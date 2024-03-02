package org.nelson.paymentservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentRequest {
    private long orderId;
    private long amount;
    private String refNumber;
    private PaymentMode paymentMode;
}
