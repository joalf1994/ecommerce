package com.escuelajavag4.order_service.service;

import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.dto.OrderItemRequestDto;
import com.escuelajavag4.order_service.model.OrderItem;

import java.util.List;

public interface IOrderItemService {
    List<OrderItemDto> orderItemsByProductId(Long productId);
    OrderItemDto findById(Long id);
    OrderItemDto createOrderItem(OrderItem request);
    OrderItemDto createOrderItem(OrderItemRequestDto request);

}
