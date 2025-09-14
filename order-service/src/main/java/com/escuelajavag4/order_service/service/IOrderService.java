package com.escuelajavag4.order_service.service;

import com.escuelajavag4.order_service.dto.OrderDto;
import com.escuelajavag4.order_service.dto.OrderRequestDto;

public interface IOrderService {
    OrderDto findAll();
    OrderDto findById(Long id);
    OrderDto createOrder(OrderRequestDto request);
}
