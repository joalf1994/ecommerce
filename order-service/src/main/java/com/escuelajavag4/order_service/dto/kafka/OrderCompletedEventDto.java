package com.escuelajavag4.order_service.dto.kafka;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderCompletedEventDto {
    private Long orderId;
    private BigDecimal total;
}
