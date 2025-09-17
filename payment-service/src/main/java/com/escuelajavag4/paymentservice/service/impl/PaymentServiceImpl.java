package com.escuelajavag4.paymentservice.service.impl;

import com.escuelajavag4.paymentservice.exception.DuplicateResourceException;
import com.escuelajavag4.paymentservice.exception.ResourceNotFoundException;
import com.escuelajavag4.paymentservice.mapper.PaymentMapper;
import com.escuelajavag4.paymentservice.messaging.PaymentEventProducer;
import com.escuelajavag4.paymentservice.model.dto.PaymentCompletedEvent;
import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentResponseDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentUpdateRequestDto;
import com.escuelajavag4.paymentservice.model.entity.Payment;
import com.escuelajavag4.paymentservice.model.entity.PaymentStatus;
import com.escuelajavag4.paymentservice.repository.PaymentRepository;
import com.escuelajavag4.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentEventProducer paymentEventProducer;
    @Override
    public PaymentResponseDto create(PaymentCreateRequestDto dto) {
        paymentRepository.findByOrderId(dto.getOrderId())
                .ifPresent(p -> {
                    throw new DuplicateResourceException(
                            "PAYMENT_ALREADY_EXISTS",
                            "Un pago ya existe para orderId: ",
                            dto.getOrderId()
                    );
                });


        if (dto.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }


        Payment payment = paymentMapper.toEntity(dto);
        payment.setStatus(PaymentStatus.COMPLETED);
        Payment saved = paymentRepository.save(payment);


        PaymentCompletedEvent  event = new PaymentCompletedEvent();
        event.setOrderId(saved.getOrderId());
        event.setAmount(saved.getAmount());
        event.setPaymentId(saved.getPaymentId());

        paymentEventProducer.emiter(event);

        return paymentMapper.toResponseDto(saved);
    }



    @Override
    public PaymentResponseDto updateStatus(Long paymentId, PaymentUpdateRequestDto dto) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("PAYMENT_NOT_FOUND",
                        "No se encontró el pago con id: " + paymentId));

        paymentMapper.updateEntityFromDto(dto, payment);
        Payment updated = paymentRepository.save(payment);
        return paymentMapper.toResponseDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto findById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("PAYMENT_NOT_FOUND",
                        "No se encontró el pago con id: " + paymentId));
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> findAll() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new ResourceNotFoundException("PAYMENT_NOT_FOUND",
                    "No se encontró el pago con id: " + paymentId);
        }
        paymentRepository.deleteById(paymentId);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto findByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("PAYMENT_NOT_FOUND",
                        "No se encontró el pago para orderId: " + orderId));
        return paymentMapper.toResponseDto(payment);
    }
}