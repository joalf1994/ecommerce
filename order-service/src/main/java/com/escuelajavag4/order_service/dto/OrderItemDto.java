package com.escuelajavag4.order_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderItemDto {
    private Long productId;
    private Integer qty;
    private Double unitPrice;
    private Double subtotal;
}
