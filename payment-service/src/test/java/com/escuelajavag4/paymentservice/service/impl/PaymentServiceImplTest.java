package com.escuelajavag4.paymentservice.service.impl;

import com.escuelajavag4.paymentservice.exception.DuplicateResourceException;
import com.escuelajavag4.paymentservice.exception.ResourceNotFoundException;
import com.escuelajavag4.paymentservice.mapper.PaymentMapper;
import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentResponseDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentUpdateRequestDto;
import com.escuelajavag4.paymentservice.model.entity.Payment;
import com.escuelajavag4.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private PaymentServiceImpl paymentService;

    // ================== CREAR ==================
    @Test
    void crear_pago_existente_lanza_excepcion() {
        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setOrderId(1L);

        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(new Payment()));

        assertThrows(DuplicateResourceException.class, () -> paymentService.create(dto));
    }

    @Test
    void crear_pago_exitoso_devuelve_dto() {
        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setOrderId(1L);

        Payment payment = new Payment();
        Payment savedPayment = new Payment();
        PaymentResponseDto responseDto = new PaymentResponseDto();

        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(savedPayment);
        when(paymentMapper.toResponseDto(savedPayment)).thenReturn(responseDto);

        PaymentResponseDto resultado = paymentService.create(dto);

        assertEquals(responseDto, resultado);
        verify(paymentRepository).save(payment);
    }

    // ================== ACTUALIZAR ESTADO ==================
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

    // ================== BUSCAR POR ID ==================
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

    // ================== BUSCAR TODOS ==================
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

    // ================== ELIMINAR ==================
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

    // ================== BUSCAR POR ORDER ID ==================
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