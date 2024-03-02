package org.nelson.orderservice.external.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nelson.orderservice.dto.PaymentMode;

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
