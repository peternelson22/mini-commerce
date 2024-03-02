package org.nelson.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    @SequenceGenerator(name = "payment_seq", allocationSize = 1, sequenceName = "payment_seq")
    private long id;
    private long amount;
    private long orderId;
    private String paymentMode;
    private String refNumber;
    private String paymentStatus;

    private Instant paymentDate;


}
