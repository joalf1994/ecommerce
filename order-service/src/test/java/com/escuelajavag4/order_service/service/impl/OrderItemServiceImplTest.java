package com.escuelajavag4.order_service.service.impl;

import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.mapper.IOrderItemMapper;
import com.escuelajavag4.order_service.model.OrderItem;
import com.escuelajavag4.order_service.repository.IOrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceImplTest {

    @Mock
    private IOrderItemRepository orderItemRepository;
    @Mock
    private IOrderItemMapper orderItemMapper;
    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    private OrderItem orderItem;
    private OrderItemDto orderItemDto;

    @BeforeEach
    void setUp() {
        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProductId(10L);
        orderItem.setQty(2);
        orderItem.setUnitPrice(BigDecimal.valueOf(50));
        orderItem.setSubtotal(BigDecimal.valueOf(100));

        orderItemDto = OrderItemDto.builder()
                .productId(10L)
                .qty(2)
                .unitPrice(50.0)
                .subtotal(100.0)
                .build();
    }

    @Test
    void createOrderItem_shouldSaveAndReturnDto() {
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        OrderItemDto result = orderItemService.createOrderItem(orderItem);

        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(10L);
        assertThat(result.getQty()).isEqualTo(2);
        assertThat(result.getUnitPrice()).isEqualTo(50.0);
        assertThat(result.getSubtotal()).isEqualTo(100.0);

        verify(orderItemRepository).save(orderItem);
        verify(orderItemMapper).toDto(orderItem);
    }

}