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
import com.example.payment.dto.NotificationDTO;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class PaymentServiceImpl implements PaymentService {

        private final String stripeSecretKey;
        private final String currency;
        private final TransactionRepository transactionRepository;
        private final RestTemplate restTemplate;

        @Value("${notification.service.url}")
        private String notificationServiceUrl;

        public PaymentServiceImpl(
                        @Value("${stripe.secret-key}") String stripeSecretKey,
                        @Value("${stripe.currency}") String currency,
                        TransactionRepository transactionRepository,
                        RestTemplate restTemplate) {
                this.stripeSecretKey = stripeSecretKey;
                this.currency = currency;
                this.transactionRepository = transactionRepository;
                this.restTemplate = restTemplate;
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

                // if ("succeeded".equals(intent.getStatus())) {
                // sendNotificationToNotifyService(request); // Notify via REST
                // }

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

        private void sendNotificationToNotifyService(PaymentRequest request) {
                NotificationDTO dto = NotificationDTO.builder()
                                .bookingId(request.getBookingId())
                                .attendeeName(request.getAttendeeName())
                                .userEmail(request.getUserEmail())
                                .eventName(request.getEventName())
                                .eventDate(request.getEventDate())
                                .eventTime(request.getEventTime())
                                .venue(request.getVenue())
                                .eventId(request.getEventId())
                                .build();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<NotificationDTO> entity = new HttpEntity<>(dto, headers);

                restTemplate.postForEntity(notificationServiceUrl + "/notify/payment-success", entity, String.class);
                restTemplate.postForEntity(notificationServiceUrl + "/notify/booking", entity, String.class);
        }
}
