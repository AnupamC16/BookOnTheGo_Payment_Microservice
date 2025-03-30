package com.example.payment.service;

import com.example.payment.dto.PaymentRequest;
import com.example.payment.dto.PaymentResponse;
import com.example.payment.model.Transaction;
import com.example.payment.repository.TransactionRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

        private final String stripeSecretKey;
        private final String currency;
        private final TransactionRepository transactionRepository;

        public PaymentServiceImpl(
                        @Value("${stripe.secret-key}") String stripeSecretKey,
                        @Value("${stripe.currency}") String currency,
                        TransactionRepository transactionRepository) {
                this.stripeSecretKey = stripeSecretKey;
                this.currency = currency;
                this.transactionRepository = transactionRepository;
                Stripe.apiKey = this.stripeSecretKey;
        }

        @Override
        public PaymentResponse processPayment(PaymentRequest request) throws StripeException {
                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                                .setAmount(request.getAmount() * 100L)
                                .setCurrency(this.currency)
                                .setPaymentMethod(request.getPaymentMethodId())
                                .setConfirm(true)
                                .setAutomaticPaymentMethods(
                                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                                                .setEnabled(true)
                                                                .setAllowRedirects(
                                                                                PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                                                .build())
                                .build();

                PaymentIntent intent = PaymentIntent.create(params);

                saveTransaction(intent, request);

                PaymentResponse response = new PaymentResponse();
                response.setStatus(intent.getStatus());

                return response;
        }

        private void saveTransaction(PaymentIntent intent, PaymentRequest request) {
                Transaction txn = new Transaction();
                txn.setPaymentIntentId(intent.getId());
                txn.setPaymentMethodId(request.getPaymentMethodId());
                txn.setAmount(request.getAmount());
                txn.setCurrency(currency);
                txn.setStatus(intent.getStatus());
                transactionRepository.save(txn);
        }
}
