package com.escuelajavag4.paymentservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentCompletedEvent {
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
}
