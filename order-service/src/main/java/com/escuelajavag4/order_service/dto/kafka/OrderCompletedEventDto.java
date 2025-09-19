package com.escuelajavag4.order_service.dto.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedEventDto {
    private Long orderId;
    private BigDecimal amount;
    private String email;
}
