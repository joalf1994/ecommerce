package com.escuelajavag4.order_service.dto.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PaymentDto {

    private Long paymentId;
    //private PaymentStatus status;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}