package com.escuelajavag4.paymentservice.controller;

import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentResponseDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentUpdateRequestDto;
import com.escuelajavag4.paymentservice.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentResponseDto paymentDto;
    private List<PaymentResponseDto> listaPagos;

    @BeforeEach
    void setUp() {
        paymentDto = new PaymentResponseDto();
        listaPagos = List.of(paymentDto);
    }

    // ================== CREAR ==================
    @Test
    void crear_pago_llama_servicio_y_devuelve_dto() {
        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        when(paymentService.create(dto)).thenReturn(paymentDto);

        PaymentResponseDto resultado = paymentController.create(dto);

        assertEquals(paymentDto, resultado);
        verify(paymentService).create(dto);
    }

    // ================== ACTUALIZAR ESTADO ==================
    @Test
    void actualizar_estado_llama_servicio_y_devuelve_dto() {
        PaymentUpdateRequestDto dto = new PaymentUpdateRequestDto();
        when(paymentService.updateStatus(1L, dto)).thenReturn(paymentDto);

        PaymentResponseDto resultado = paymentController.updateStatus(1L, dto);

        assertEquals(paymentDto, resultado);
        verify(paymentService).updateStatus(1L, dto);
    }

    // ================== BUSCAR POR ID ==================
    @Test
    void buscar_por_id_llama_servicio_y_devuelve_dto() {
        when(paymentService.findById(1L)).thenReturn(paymentDto);

        PaymentResponseDto resultado = paymentController.findById(1L);

        assertEquals(paymentDto, resultado);
        verify(paymentService).findById(1L);
    }

    // ================== BUSCAR POR ORDER ID ==================
    @Test
    void buscar_por_order_id_llama_servicio_y_devuelve_dto() {
        when(paymentService.findByOrderId(1L)).thenReturn(paymentDto);

        PaymentResponseDto resultado = paymentController.findByOrderId(1L);

        assertEquals(paymentDto, resultado);
        verify(paymentService).findByOrderId(1L);
    }

    // ================== BUSCAR TODOS ==================
    @Test
    void buscar_todos_llama_servicio_y_devuelve_lista() {
        when(paymentService.findAll()).thenReturn(listaPagos);

        List<PaymentResponseDto> resultado = paymentController.findAll();

        assertEquals(listaPagos, resultado);
        verify(paymentService).findAll();
    }

    // ================== ELIMINAR ==================
    @Test
    void eliminar_llama_servicio() {
        doNothing().when(paymentService).delete(1L);

        paymentController.delete(1L);

        verify(paymentService).delete(1L);
    }
}
