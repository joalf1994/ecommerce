package com.escuelajavag4.order_service.service.impl;

import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.mapper.IOrderItemMapper;
import com.escuelajavag4.order_service.model.OrderItem;
import com.escuelajavag4.order_service.repository.IOrderItemRepository;
import com.escuelajavag4.order_service.service.IOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements IOrderItemService {

    private final IOrderItemRepository orderItemRepository;
    private final IOrderItemMapper orderItemMapper;

    @Override
    public OrderItemDto createOrderItem(OrderItem request) {
        OrderItem savedOrderItem = orderItemRepository.save(request);
        return orderItemMapper.toDto(savedOrderItem);
    }

}
