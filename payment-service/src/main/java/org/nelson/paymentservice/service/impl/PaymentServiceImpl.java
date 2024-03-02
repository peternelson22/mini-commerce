package org.nelson.paymentservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nelson.paymentservice.dto.PaymentMode;
import org.nelson.paymentservice.dto.PaymentRequest;
import org.nelson.paymentservice.dto.PaymentResponse;
import org.nelson.paymentservice.entity.Payment;
import org.nelson.paymentservice.repository.PaymentRepository;
import org.nelson.paymentservice.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@Slf4j
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;
    @Override
    public void doPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment...{}", paymentRequest.getOrderId());
        Payment payment = Payment.builder()
                .orderId(paymentRequest.getOrderId())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentDate(Instant.now())
                .refNumber(paymentRequest.getRefNumber())
                .paymentStatus("SUCCESS")
                .amount(paymentRequest.getAmount())
                .build();

        paymentRepository.save(payment);
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId);

        log.info("Getting payment details for orderId {}", orderId);
        return PaymentResponse.builder()
                .paymentMode(PaymentMode.valueOf(payment.getPaymentMode()))
                .paymentId(payment.getId())
                .status(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .orderId(payment.getOrderId())
                .build();
    }
}
