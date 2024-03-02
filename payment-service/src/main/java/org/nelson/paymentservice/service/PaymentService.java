package org.nelson.paymentservice.service;

import org.nelson.paymentservice.dto.PaymentRequest;
import org.nelson.paymentservice.dto.PaymentResponse;

public interface PaymentService {
    void doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(long orderId);
}
