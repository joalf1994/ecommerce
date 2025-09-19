package com.escuelajavag4.paymentservice.service.impl;

import com.escuelajavag4.paymentservice.exception.DuplicateResourceException;
import com.escuelajavag4.paymentservice.exception.ResourceNotFoundException;
import com.escuelajavag4.paymentservice.mapper.PaymentMapper;
import com.escuelajavag4.paymentservice.messaging.PaymentEventProducer;
import com.escuelajavag4.paymentservice.model.dto.*;
import com.escuelajavag4.paymentservice.model.entity.Payment;
import com.escuelajavag4.paymentservice.model.entity.PaymentStatus;
import com.escuelajavag4.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private PaymentEventProducer paymentEventProducer;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void crear_deuda_existente_lanza_excepcion() {
        OrderCompletedEventDto dto = new OrderCompletedEventDto();
        dto.setOrderId(1L);

        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(new Payment()));

        assertThrows(DuplicateResourceException.class, () -> paymentService.createDeuda(dto));
    }

    @Test
    void crear_deuda_exitoso_devuelve_dto() {
        OrderCompletedEventDto dto = new OrderCompletedEventDto();
        dto.setOrderId(1L);
        dto.setAmount(BigDecimal.valueOf(100));

        Payment savedPayment = new Payment();
        PaymentResponseDto responseDto = new PaymentResponseDto();

        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(paymentMapper.toResponseDto(savedPayment)).thenReturn(responseDto);

        PaymentResponseDto resultado = paymentService.createDeuda(dto);

        assertEquals(responseDto, resultado);
        verify(paymentRepository).save(any(Payment.class));
        verifyNoInteractions(paymentEventProducer);
    }

    @Test
    void crear_deuda_cantidad_negativa_lanza_excepcion() {
        OrderCompletedEventDto dto = new OrderCompletedEventDto();
        dto.setOrderId(1L);
        dto.setAmount(BigDecimal.valueOf(-50));

        assertThrows(IllegalArgumentException.class, () -> paymentService.createDeuda(dto));
    }

    @Test
    void process_payment_exitoso_pago_total() {
        Payment deuda = new Payment();
        deuda.setOrderId(1L);
        deuda.setAmount(BigDecimal.valueOf(100));
        deuda.setStatus(PaymentStatus.PENDING);

        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setAmount(BigDecimal.valueOf(100));

        Payment pago = new Payment();
        pago.setOrderId(1L);
        pago.setAmount(BigDecimal.valueOf(100));
        pago.setStatus(PaymentStatus.COMPLETED);

        PaymentResponseDto responseDto = new PaymentResponseDto();

        when(paymentRepository.findAllByOrderId(1L)).thenReturn(List.of(deuda));
        when(paymentRepository.save(any(Payment.class))).thenReturn(pago);
        when(paymentMapper.toResponseDto(pago)).thenReturn(responseDto);

        PaymentResponseDto resultado = paymentService.processPayment(dto);

        assertEquals(responseDto, resultado);
        verify(paymentRepository, times(2)).save(any(Payment.class));
        verify(paymentEventProducer).emiter(any(PaymentCompletedEvent.class));
    }

    @Test
    void process_payment_exitoso_pago_parcial() {
        Payment deuda = new Payment();
        deuda.setOrderId(1L);
        deuda.setAmount(BigDecimal.valueOf(100));
        deuda.setStatus(PaymentStatus.PENDING);

        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setAmount(BigDecimal.valueOf(40));

        Payment pago = new Payment();
        pago.setOrderId(1L);
        pago.setAmount(BigDecimal.valueOf(40));
        pago.setStatus(PaymentStatus.COMPLETED);

        PaymentResponseDto responseDto = new PaymentResponseDto();

        when(paymentRepository.findAllByOrderId(1L)).thenReturn(List.of(deuda));
        when(paymentRepository.save(any(Payment.class))).thenReturn(pago);
        when(paymentMapper.toResponseDto(pago)).thenReturn(responseDto);

        PaymentResponseDto resultado = paymentService.processPayment(dto);

        assertEquals(responseDto, resultado);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(paymentEventProducer).emiter(any(PaymentCompletedEvent.class));
    }

    @Test
    void process_payment_pago_mayor_a_deuda_lanza_excepcion() {
        Payment deuda = new Payment();
        deuda.setOrderId(1L);
        deuda.setAmount(BigDecimal.valueOf(100));
        deuda.setStatus(PaymentStatus.PENDING);

        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setAmount(BigDecimal.valueOf(120));

        when(paymentRepository.findAllByOrderId(1L)).thenReturn(List.of(deuda));

        assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(dto));
    }

    @Test
    void process_payment_sin_deuda_lanza_excepcion() {
        when(paymentRepository.findAllByOrderId(1L)).thenReturn(List.of());

        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setAmount(BigDecimal.valueOf(50));

        assertThrows(ResourceNotFoundException.class, () -> paymentService.processPayment(dto));
    }

    @Test
    void actualizar_estado_pago_no_encontrado_lanza_excepcion() {
        PaymentUpdateRequestDto dto = new PaymentUpdateRequestDto();
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.updateStatus(1L, dto));
    }

    @Test
    void actualizar_estado_pago_exitoso_devuelve_dto() {
        PaymentUpdateRequestDto dto = new PaymentUpdateRequestDto();
        Payment payment = new Payment();
        Payment updatedPayment = new Payment();
        PaymentResponseDto responseDto = new PaymentResponseDto();

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        doNothing().when(paymentMapper).updateEntityFromDto(dto, payment);
        when(paymentRepository.save(payment)).thenReturn(updatedPayment);
        when(paymentMapper.toResponseDto(updatedPayment)).thenReturn(responseDto);

        PaymentResponseDto resultado = paymentService.updateStatus(1L, dto);

        assertEquals(responseDto, resultado);
        verify(paymentRepository).save(payment);
    }

    @Test
    void buscar_por_id_no_encontrado_lanza_excepcion() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.findById(1L));
    }

    @Test
    void buscar_por_id_exitoso_devuelve_dto() {
        Payment payment = new Payment();
        PaymentResponseDto responseDto = new PaymentResponseDto();

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toResponseDto(payment)).thenReturn(responseDto);

        PaymentResponseDto resultado = paymentService.findById(1L);

        assertEquals(responseDto, resultado);
    }

    @Test
    void buscar_todos_devuelve_lista_de_dtos() {
        Payment payment1 = new Payment();
        Payment payment2 = new Payment();
        List<Payment> pagos = List.of(payment1, payment2);

        PaymentResponseDto dto1 = new PaymentResponseDto();
        PaymentResponseDto dto2 = new PaymentResponseDto();

        when(paymentRepository.findAll()).thenReturn(pagos);
        when(paymentMapper.toResponseDto(payment1)).thenReturn(dto1);
        when(paymentMapper.toResponseDto(payment2)).thenReturn(dto2);

        List<PaymentResponseDto> resultado = paymentService.findAll();

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(dto1));
        assertTrue(resultado.contains(dto2));
    }

    @Test
    void eliminar_pago_no_encontrado_lanza_excepcion() {
        when(paymentRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> paymentService.delete(1L));
    }

    @Test
    void eliminar_pago_exitoso_llama_a_delete() {
        when(paymentRepository.existsById(1L)).thenReturn(true);

        paymentService.delete(1L);

        verify(paymentRepository).deleteById(1L);
    }

    @Test
    void buscar_por_order_id_no_encontrado_lanza_excepcion() {
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.findByOrderId(1L));
    }

    @Test
    void buscar_por_order_id_exitoso_devuelve_lista_dto() {
        Payment payment = new Payment();
        PaymentResponseDto responseDto = new PaymentResponseDto();

        List<Payment> payments = List.of(payment);

        when(paymentRepository.findAllByOrderId(1L)).thenReturn(payments);
        when(paymentMapper.toResponseDto(payment)).thenReturn(responseDto);

        List<PaymentResponseDto> resultado = paymentService.findAllByOrderId(1L);

        assertEquals(1, resultado.size());
        assertEquals(responseDto, resultado.get(0));
    }
}