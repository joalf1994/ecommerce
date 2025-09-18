package com.escuelajavag4.notification_service.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentCompletedEvent {
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private String status;
    private String email;
}
