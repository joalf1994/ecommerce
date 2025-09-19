package com.escuelajavag4.order_service.service;

import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.dto.OrderItemRequestDto;
import com.escuelajavag4.order_service.model.OrderItem;

import java.util.List;

public interface IOrderItemService {
    OrderItemDto createOrderItem(OrderItem request);

}
