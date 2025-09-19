package com.escuelajavag4.paymentservice.repository;

import com.escuelajavag4.paymentservice.model.entity.Payment;
import com.escuelajavag4.paymentservice.model.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByOrderId(Long orderId);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findAllByOrderId(Long orderId);
}
