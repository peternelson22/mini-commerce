package org.nelson.orderservice.external.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.nelson.orderservice.exception.OrderApiException;
import org.nelson.orderservice.external.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/api/payment")
public interface PaymentService {

    @PostMapping
    ResponseEntity<String> doPayment(@RequestBody PaymentRequest paymentRequest);

    default void fallback(){
        throw new OrderApiException("Payment service unavailable", "SERVICE_UNAVAILABLE", 500);
    }
    }
