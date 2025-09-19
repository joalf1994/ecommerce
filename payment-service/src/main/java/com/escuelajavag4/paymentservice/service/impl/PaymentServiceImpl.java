package com.escuelajavag4.paymentservice.service.impl;

import com.escuelajavag4.paymentservice.exception.DuplicateResourceException;
import com.escuelajavag4.paymentservice.exception.ResourceNotFoundException;
import com.escuelajavag4.paymentservice.mapper.PaymentMapper;
import com.escuelajavag4.paymentservice.messaging.PaymentEventProducer;
import com.escuelajavag4.paymentservice.model.dto.*;
import com.escuelajavag4.paymentservice.model.entity.Payment;
import com.escuelajavag4.paymentservice.model.entity.PaymentStatus;
import com.escuelajavag4.paymentservice.repository.PaymentRepository;
import com.escuelajavag4.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentEventProducer paymentEventProducer;
    private final Map<Long, String> orderEmailCache = new ConcurrentHashMap<>();


    @Override
    public PaymentResponseDto createDeuda(OrderCompletedEventDto dto) {
        paymentRepository.findByOrderId(dto.getOrderId())
                .ifPresent(p -> {
                    throw new DuplicateResourceException(
                            "PAYMENT_ALREADY_EXISTS",
                            "La deuda ya existe para orderId: ",
                            dto.getOrderId()
                    );
                });

        if (dto.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La deuda no puede ser negativa");
        }

        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        payment.setAmount(dto.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        Payment saved = paymentRepository.save(payment);

        return paymentMapper.toResponseDto(saved);
    }

    @Override
    public PaymentResponseDto processPayment(Long orderId, PaymentCreateRequestDto dto) {
        List<Payment> pagosAndDeudas = paymentRepository.findAllByOrderId(orderId);

        if (pagosAndDeudas.isEmpty()) {
            throw new ResourceNotFoundException("PAYMENT_NOT_FOUND",
                    "No se encontró nada registrado para la orden: " + orderId);
        }

        Payment deudaOriginal = pagosAndDeudas.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PENDING || p.getStatus() == PaymentStatus.PAID_OFF)
                .min(Comparator.comparing(Payment::getCreatedAt))
                .orElseThrow(() -> new IllegalStateException("No se encontró la deuda original"));

        if (deudaOriginal.getStatus() == PaymentStatus.PAID_OFF) {
            throw new IllegalStateException("La deuda ya está completamente pagada");
        }

        List<Payment> pagosRealizados = pagosAndDeudas.stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                .toList();

        BigDecimal totalPagado = pagosRealizados.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = deudaOriginal.getAmount().subtract(totalPagado);

        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("La deuda ya está completamente pagada");
        }

        if (dto.getAmount().compareTo(balance) > 0) {
            throw new IllegalArgumentException("El pago excede la deuda restante. " +
                    "Restante: " + balance + ", Intentado: " + dto.getAmount());
        }

        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }

        Payment newPayment = new Payment();
        newPayment.setOrderId(orderId);
        newPayment.setAmount(dto.getAmount());
        newPayment.setStatus(PaymentStatus.COMPLETED);

        Payment savedPayment = paymentRepository.save(newPayment);

        BigDecimal nuevoBalance = balance.subtract(dto.getAmount());
        if (nuevoBalance.compareTo(BigDecimal.ZERO) == 0) {
            deudaOriginal.setStatus(PaymentStatus.PAID_OFF);
            paymentRepository.save(deudaOriginal);
        }
        String email = orderEmailCache.get(orderId);
        launchEvent(savedPayment, email);

        if (nuevoBalance.compareTo(BigDecimal.ZERO) == 0) {
            orderEmailCache.remove(orderId);
        }
        return paymentMapper.toResponseDto(savedPayment);
    }

    private void launchEvent(Payment updated, String email) {
        PaymentCompletedEvent event = new PaymentCompletedEvent();
        event.setOrderId(updated.getOrderId());
        event.setAmount(updated.getAmount());
        event.setPaymentId(updated.getPaymentId());
        event.setStatus(updated.getStatus().name());
        event.setEmail(email);
        paymentEventProducer.emiter(event);
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

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> findByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status)
                .stream()
                .map(paymentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public void cacheOrderEmail(Long orderId, String email) {
        orderEmailCache.put(orderId, email);
    }


}