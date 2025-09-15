package com.escuelajavag4.paymentservice.service;

import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentResponseDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentUpdateRequestDto;

import java.util.List;

public interface PaymentService {

    // Crear un pago
    PaymentResponseDto create(PaymentCreateRequestDto dto);

    // Actualizar estado de un pago
    PaymentResponseDto updateStatus(Long paymentId, PaymentUpdateRequestDto dto);

    // Obtener un pago por su ID
    PaymentResponseDto findById(Long paymentId);

    // Obtener todos los pagos
    List<PaymentResponseDto> findAll();

    // Eliminar un pago (opcional, dependiendo de tu negocio)
    void delete(Long paymentId);

    // Opcional: buscar pago por orderId
    PaymentResponseDto findByOrderId(Long orderId);
}
