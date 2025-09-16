package com.escuelajavag4.order_service.mapper;

import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.dto.OrderItemRequestDto;
import com.escuelajavag4.order_service.model.Order;
import com.escuelajavag4.order_service.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IOrderItemMapper {

    OrderItem toEntity(OrderItemRequestDto dto, Order order);

    OrderItemDto toDto(OrderItem entity);
}
