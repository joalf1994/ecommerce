package com.escuelajavag4.paymentservice.service;

import com.escuelajavag4.paymentservice.model.dto.OrderCompletedEventDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentResponseDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentUpdateRequestDto;
import com.escuelajavag4.paymentservice.model.entity.PaymentStatus;

import java.util.List;

public interface PaymentService {

    PaymentResponseDto createDeuda(OrderCompletedEventDto dto);

    PaymentResponseDto updateStatus(Long paymentId, PaymentUpdateRequestDto dto);

    PaymentResponseDto findById(Long paymentId);

    List<PaymentResponseDto> findAll();

    PaymentResponseDto processPayment(Long paymentId, PaymentCreateRequestDto dto);
    void delete(Long paymentId);

    PaymentResponseDto findByOrderId(Long orderId);

    List<PaymentResponseDto> findByStatus(PaymentStatus status);

}
