package org.nelson.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private long orderId;
    private String orderStatus;
    private Instant orderDate;
    private long amount;
    private ProductDetails productDetails;
    private PaymentDetails paymentDetails;

    @AllArgsConstructor @Data
    @NoArgsConstructor @Builder
    public static class ProductDetails {

        private long id;
        private String name;
        private long price;
        private long quantity;

    }

    @AllArgsConstructor @Data
    @NoArgsConstructor @Builder
    public static class PaymentDetails {
        private long paymentId;
        private String status;
        private PaymentMode paymentMode;
        private Instant paymentDate;
    }

}
