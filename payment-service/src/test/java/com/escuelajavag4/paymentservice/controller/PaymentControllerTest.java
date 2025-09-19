package com.escuelajavag4.paymentservice.controller;

import com.escuelajavag4.paymentservice.model.dto.OrderCompletedEventDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    @Test
    void crear_deuda_llama_servicio_y_devuelve_dto() {
        OrderCompletedEventDto dto = new OrderCompletedEventDto();
        when(paymentService.createDeuda(dto)).thenReturn(paymentDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.create(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(paymentDto, response.getBody());
        verify(paymentService).createDeuda(dto);
    }

    @Test
    void procesar_pago_llama_servicio_y_devuelve_dto() {
        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        when(paymentService.processPayment(dto)).thenReturn(paymentDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.processPayment(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentDto, response.getBody());
        verify(paymentService).processPayment(dto);
    }

    @Test
    void actualizar_estado_llama_servicio_y_devuelve_dto() {
        PaymentUpdateRequestDto dto = new PaymentUpdateRequestDto();
        when(paymentService.updateStatus(1L, dto)).thenReturn(paymentDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.updateStatus(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentDto, response.getBody());
        verify(paymentService).updateStatus(1L, dto);
    }

    @Test
    void buscar_por_id_llama_servicio_y_devuelve_dto() {
        when(paymentService.findById(1L)).thenReturn(paymentDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentDto, response.getBody());
        verify(paymentService).findById(1L);
    }

    @Test
    void buscar_por_order_id_llama_servicio_y_devuelve_lista_dto() {
        List<PaymentResponseDto> paymentDtos = List.of(paymentDto);

        when(paymentService.findAllByOrderId(1L)).thenReturn(paymentDtos);

        ResponseEntity<List<PaymentResponseDto>> response = paymentController.findByOrderId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentDtos, response.getBody());
        verify(paymentService).findByOrderId(1L);
    }

    @Test
    void buscar_todos_llama_servicio_y_devuelve_lista() {
        when(paymentService.findAll()).thenReturn(listaPagos);

        ResponseEntity<List<PaymentResponseDto>> response = paymentController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listaPagos, response.getBody());
        verify(paymentService).findAll();
    }

    @Test
    void eliminar_llama_servicio_y_devuelve_no_content() {
        doNothing().when(paymentService).delete(1L);

        ResponseEntity<Void> response = paymentController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(paymentService).delete(1L);
    }
}
