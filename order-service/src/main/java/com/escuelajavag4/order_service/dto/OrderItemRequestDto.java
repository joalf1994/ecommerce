package com.escuelajavag4.order_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderItemRequestDto {
    private String productId;
    private Integer qty;
}
