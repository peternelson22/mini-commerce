package org.nelson.paymentservice.repository;

import org.nelson.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByOrderId(long orderId);
}
