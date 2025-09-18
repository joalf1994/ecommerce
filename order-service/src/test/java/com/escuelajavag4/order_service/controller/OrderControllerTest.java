package com.escuelajavag4.order_service.controller;

import com.escuelajavag4.order_service.dto.OrderDto;
import com.escuelajavag4.order_service.dto.OrderRequestDto;
import com.escuelajavag4.order_service.model.PaymentMethod;
import com.escuelajavag4.order_service.service.IOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private IOrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Long id;
    private OrderDto orderDto;
    private OrderRequestDto orderRequestDto;

    @BeforeEach
    void setUp() {
        id = 1L;

        orderDto = OrderDto.builder()
                .id(id)
                .status("CREATED")
                .createdAt(null)
                .total(1000.0)
                .build();

        orderRequestDto = OrderRequestDto.builder()
                .customerId(1L)
                .items(null)
                .paymentMethod(PaymentMethod.VISA)
                .build();
    }

    @Test
    void findById() {
        when(orderService.findOrderById(id)).thenReturn(orderDto);

        orderDto = orderController.findById(id);

        assertThat(orderDto).isNotNull();
        assertThat(orderDto.getId()).isEqualTo(id);

        verify(orderService, times(1)).findOrderById(id);
    }

    @Test
    void createOrder() {
        when(orderService.createOrder(orderRequestDto)).thenReturn(orderDto);

        OrderDto result = orderController.createOrder(orderRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);

        verify(orderService, times(1)).createOrder(orderRequestDto);
    }
}