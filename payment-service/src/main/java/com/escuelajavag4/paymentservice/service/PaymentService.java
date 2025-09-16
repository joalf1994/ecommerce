package com.escuelajavag4.paymentservice.service;

import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentResponseDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentUpdateRequestDto;

import java.util.List;

public interface PaymentService {

    PaymentResponseDto create(PaymentCreateRequestDto dto);

    PaymentResponseDto updateStatus(Long paymentId, PaymentUpdateRequestDto dto);

    PaymentResponseDto findById(Long paymentId);

    List<PaymentResponseDto> findAll();

    void delete(Long paymentId);

    PaymentResponseDto findByOrderId(Long orderId);
}
