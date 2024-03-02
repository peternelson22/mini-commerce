package org.nelson.paymentservice.controller;

import lombok.AllArgsConstructor;
import org.nelson.paymentservice.dto.PaymentRequest;
import org.nelson.paymentservice.dto.PaymentResponse;
import org.nelson.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/payment")
@AllArgsConstructor
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> doPayment(@RequestBody PaymentRequest paymentRequest){
        paymentService.doPayment(paymentRequest);
        return new ResponseEntity<>("Payment Successful", CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailsByOrderId(@PathVariable long orderId){
        return new ResponseEntity<>(paymentService.getPaymentDetailsByOrderId(orderId), OK);
    }

}
