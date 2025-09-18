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
    void process_payment_pago_exacto_completa_pago() {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.PENDING);

        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setAmount(BigDecimal.valueOf(100));

        PaymentResponseDto responseDto = new PaymentResponseDto();

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toResponseDto(payment)).thenReturn(responseDto);

        PaymentResponseDto resultado = paymentService.processPayment(1L, dto);

        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(BigDecimal.ZERO, payment.getAmount());
        assertEquals(responseDto, resultado);
        verify(paymentEventProducer).emiter(any(PaymentCompletedEvent.class));
    }

    @Test
    void process_payment_pago_parcial_actualiza_deuda() {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.PENDING);

        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setAmount(BigDecimal.valueOf(40));

        PaymentResponseDto responseDto = new PaymentResponseDto();

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toResponseDto(payment)).thenReturn(responseDto);

        PaymentResponseDto resultado = paymentService.processPayment(1L, dto);

        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertEquals(BigDecimal.valueOf(60), payment.getAmount());
        assertEquals(responseDto, resultado);
        verify(paymentEventProducer).emiter(any(PaymentCompletedEvent.class));
    }

    @Test
    void process_payment_pago_mayor_a_deuda_lanza_excepcion() {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.PENDING);

        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setAmount(BigDecimal.valueOf(120));

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.processPayment(1L, dto));
    }

    @Test
    void process_payment_pago_no_pending_lanza_excepcion() {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.COMPLETED);

        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setAmount(BigDecimal.valueOf(50));

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        assertThrows(IllegalStateException.class,
                () -> paymentService.processPayment(1L, dto));
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
    void buscar_por_order_id_exitoso_devuelve_dto() {
        Payment payment = new Payment();
        PaymentResponseDto responseDto = new PaymentResponseDto();

        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(payment));
        when(paymentMapper.toResponseDto(payment)).thenReturn(responseDto);

        PaymentResponseDto resultado = paymentService.findByOrderId(1L);

        assertEquals(responseDto, resultado);
    }
}
