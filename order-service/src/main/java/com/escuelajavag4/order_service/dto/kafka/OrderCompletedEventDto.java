package com.escuelajavag4.order_service.dto.kafka;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedEventDto {
    private Long orderId;
    private BigDecimal amount;
}
