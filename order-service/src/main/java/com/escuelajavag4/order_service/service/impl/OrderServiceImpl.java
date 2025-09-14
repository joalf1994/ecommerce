package com.escuelajavag4.order_service.service.impl;

import com.escuelajavag4.order_service.dto.OrderDto;
import com.escuelajavag4.order_service.dto.OrderRequestDto;
import com.escuelajavag4.order_service.mapper.IOrderMapper;
import com.escuelajavag4.order_service.repository.IOrderRepository;
import com.escuelajavag4.order_service.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderMapper orderMapper;

    @Override
    public OrderDto findAll() {
        return null;
    }

    @Override
    public OrderDto findById(Long id) {
        return null;
    }

    @Override
    public OrderDto createOrder(OrderRequestDto request) {

        return null;
    }
}
