package com.example.payment.service;

import com.example.payment.dto.PaymentRequest;
import com.example.payment.dto.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request) throws StripeException;
}
