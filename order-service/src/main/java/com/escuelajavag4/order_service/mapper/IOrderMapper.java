package com.escuelajavag4.order_service.mapper;

import com.escuelajavag4.order_service.dto.OrderDto;
import com.escuelajavag4.order_service.dto.OrderRequestDto;
import com.escuelajavag4.order_service.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IOrderMapper {

    Order toEntity(OrderRequestDto dto);

    OrderDto toDto(Order entity);
}
