package com.escuelajavag4.order_service.dto;

import com.escuelajavag4.order_service.model.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderRequestDto {
    private Long customerId;
    private List<OrderItemRequestDto> items;
    private PaymentMethod paymentMethod;
}
