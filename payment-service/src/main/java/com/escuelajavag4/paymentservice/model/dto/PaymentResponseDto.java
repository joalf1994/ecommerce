package com.escuelajavag4.paymentservice.model.dto;


import com.escuelajavag4.paymentservice.model.entity.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponseDto {

    private Long paymentId;
    private Long orderId;
    private PaymentStatus status;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}