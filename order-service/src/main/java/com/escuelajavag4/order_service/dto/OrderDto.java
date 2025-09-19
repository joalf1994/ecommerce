package com.escuelajavag4.order_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDto {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private Double total;
    private List<OrderItemDto> items;
}