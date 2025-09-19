package com.escuelajavag4.paymentservice.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedEventDto {
    private Long orderId;
    private BigDecimal amount;
    private String email;
}
