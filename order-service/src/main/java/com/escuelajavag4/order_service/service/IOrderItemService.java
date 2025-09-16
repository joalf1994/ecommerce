package com.escuelajavag4.order_service.service;

import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.dto.OrderItemRequestDto;

import java.util.List;

public interface IOrderItemService {
    OrderItemDto findOrderItemByProductId(Long id);
    OrderItemDto createOrderItem(List<OrderItemRequestDto> request);
}
