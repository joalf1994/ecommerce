package com.escuelajavag4.paymentservice.mapper;


import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentResponseDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentUpdateRequestDto;
import com.escuelajavag4.paymentservice.model.entity.Payment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Payment toEntity(PaymentCreateRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "status", ignore = false)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(PaymentUpdateRequestDto dto, @MappingTarget Payment entity);

    PaymentResponseDto toResponseDto(Payment entity);
}
