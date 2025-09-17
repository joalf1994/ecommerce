package com.escuelajavag4.order_service.service.impl;

import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.dto.OrderItemRequestDto;
import com.escuelajavag4.order_service.exception.OrderItemNotFoundException;
import com.escuelajavag4.order_service.mapper.IOrderItemMapper;
import com.escuelajavag4.order_service.model.OrderItem;
import com.escuelajavag4.order_service.repository.IOrderItemRepository;
import com.escuelajavag4.order_service.service.IOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements IOrderItemService {

    private final IOrderItemRepository orderItemRepository;
    private final IOrderItemMapper orderItemMapper;

    @Override
    public OrderItemDto findById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException("OrderItem with id " + id + " not found"));
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public OrderItemDto createOrderItem(OrderItemRequestDto request) {
        OrderItem savedOrderItem = orderItemRepository.save(orderItemMapper.toEntity(request, null));
        return orderItemMapper.toDto(savedOrderItem);
    }

    @Override
    public OrderItemDto createOrderItem(OrderItem request) {
        OrderItem savedOrderItem = orderItemRepository.save(request);
        return orderItemMapper.toDto(savedOrderItem);
    }

    @Override
    public List<OrderItemDto> orderItemsByProductId(Long productId) {
        List<OrderItem> orderItem = orderItemRepository.findOrderItemsByProductId(productId);
        return orderItem.stream()
                .map(orderItemMapper::toDto)
                .toList();
    }
}
