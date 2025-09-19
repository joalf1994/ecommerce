package com.escuelajavag4.order_service.mapper;

import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.dto.OrderItemRequestDto;
import com.escuelajavag4.order_service.model.Order;
import com.escuelajavag4.order_service.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IOrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    OrderItem toEntity(OrderItemRequestDto dto, Order order);

    OrderItemDto toDto(OrderItem entity);
}
