package com.escuelajavag4.paymentservice.mapper;


import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentResponseDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentUpdateRequestDto;
import com.escuelajavag4.paymentservice.model.entity.Payment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    // ================== CREATE ==================
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", ignore = true)
    Payment toEntity(PaymentCreateRequestDto dto);

    // ================== UPDATE ==================
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(PaymentUpdateRequestDto dto, @MappingTarget Payment entity);

    // ================== RESPONSE ==================
    PaymentResponseDto toResponseDto(Payment entity);
}