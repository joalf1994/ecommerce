package com.escuelajavag4.order_service.service.impl;

import com.escuelajavag4.order_service.dto.*;
import com.escuelajavag4.order_service.dto.customer.CustomerDto;
import com.escuelajavag4.order_service.dto.inventory.StockReservedDto;
import com.escuelajavag4.order_service.dto.kafka.OrderCompletedEventDto;
import com.escuelajavag4.order_service.dto.product.ProductDto;
import com.escuelajavag4.order_service.exception.*;
import com.escuelajavag4.order_service.feign.CatalogClient;
import com.escuelajavag4.order_service.feign.CustomerClient;
import com.escuelajavag4.order_service.feign.InventoryClient;
import com.escuelajavag4.order_service.mapper.IOrderItemMapper;
import com.escuelajavag4.order_service.mapper.IOrderMapper;
import com.escuelajavag4.order_service.messaging.OrderEventProducer;
import com.escuelajavag4.order_service.model.Order;
import com.escuelajavag4.order_service.model.OrderItem;
import com.escuelajavag4.order_service.model.PaymentMethod;
import com.escuelajavag4.order_service.repository.IOrderRepository;
import com.escuelajavag4.order_service.service.IOrderItemService;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private
    IOrderMapper orderMapper;

    @Mock
    private
    IOrderItemMapper orderItemMapper;

    @Mock
    private IOrderItemService orderItemService;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private CatalogClient catalogClient;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private OrderEventProducer orderEventProducer;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Long orderId;
    private Long customerId;
    private Long productId;
    private Long orderItemId;
    private String email;
    private Order order;
    private OrderDto orderDto;
    private OrderRequestDto orderRequestDto;
    private OrderItem orderItem;
    private OrderItemDto orderItemDto;
    private OrderItemRequestDto orderItemRequestDto;
    private ProductDto productDto;
    private StockReservedDto stockReservedDto;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        orderId = 1L;
        customerId = 1L;
        productId = 10L;
        orderItemId = 1L;
        email = "joalf1994@email.com";

        orderItemRequestDto = OrderItemRequestDto.builder()
                .productId(productId)
                .qty(2)
                .build();

        orderRequestDto = OrderRequestDto.builder()
                .customerId(customerId)
                .items(List.of(orderItemRequestDto))
                .paymentMethod(PaymentMethod.VISA)
                .build();

        orderItem = new OrderItem();
        orderItem.setId(orderItemId);
        orderItem.setProductId(productId);
        orderItem.setQty(2);
        orderItem.setUnitPrice(BigDecimal.valueOf(50));
        orderItem.setSubtotal(BigDecimal.valueOf(100));

        order = new Order();
        order.setId(orderId);
        order.setCustomerId(customerId);
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());
        order.setTotal(100.0);
        order.setItems(List.of());

        orderItemDto = OrderItemDto.builder()
                .productId(productId)
                .qty(2)
                .unitPrice(50.0)
                .subtotal(100.0)
                .build();

        orderDto = OrderDto.builder()
                .id(orderId)
                .status("CREATED")
                .createdAt(LocalDateTime.now())
                .total(100.0)
                .items(List.of(orderItemDto))
                .build();


        productDto = ProductDto.builder()
                .id(productId)
                .active(true)
                .price(BigDecimal.valueOf(50))
                .build();

        stockReservedDto = StockReservedDto.builder()
                .status("OK")
                .reservationId("1")
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
        customerDto = CustomerDto.builder()
                .id(customerId)
                .email(email)
                .level("PREMIUM")
                .fullName("Pedro Alva Díaz")
                .build();
    }

    @Test
    void findOrderById_shouldReturnOrderDto() {
        order.setItems(List.of(orderItem));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);

        OrderDto orderById = orderService.findOrderById(orderId);

        assertThat(orderById).isNotNull();
        assertThat(orderById.getId()).isEqualTo(orderId);
        assertThat(orderById.getItems()).isNotEmpty();

        verify(orderRepository).findById(orderId);
        verify(orderMapper).toDto(order);
        verify(orderItemMapper).toDto(orderItem);
    }

    @Test
    void findOrderById_shouldThrowWhenNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findOrderById(orderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order with id " + orderId + " not found");

        verify(orderRepository).findById(orderId);
    }

    @Test
    void createOrder_shouldThrowWhenProductNotActive() {
        // Arrange
        // Cliente válido
        when(customerClient.getActiveCustomerById(customerId)).thenReturn(customerDto);
        when(customerClient.getCustomerById(customerId)).thenReturn(customerDto);
        // El mapeo debe devolver un Order válido para evitar NPE
        when(orderMapper.toEntity(orderRequestDto)).thenReturn(order);
        // Producto inactivo
        productDto.setActive(false);
        when(catalogClient.getProductById(productId)).thenReturn(productDto);

        // Act + Assert
        assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(orderRequestDto));

        // No debe reservar stock ni guardar
        verify(catalogClient).getProductById(productId);
        verify(inventoryClient, never()).hasSufficientStock(anyLong(), anyInt());
        verify(inventoryClient, never()).reserveStock(anyLong(), anyInt());
        verify(orderRepository, never()).save(any());
        verify(orderEventProducer, never()).emisorCompletedEvent(any());
        // Verifica que no se mapean ni crean ítems
        verify(orderItemMapper, never()).toEntity(any(), any());
        verify(orderItemService, never()).createOrderItem(any());
    }


    @Test
    void createOrder_shouldThrowWhenCustomerNotFound() {
        // Arrange: cliente no encontrado
        when(customerClient.getActiveCustomerById(customerId)).thenReturn(null);

        // Act + Assert
        assertThrows(CustomerNotFoundException.class, () -> orderService.createOrder(orderRequestDto));

        // Se valida que solo intentó obtener el cliente y no continuó
        verify(customerClient).getActiveCustomerById(customerId);
        verifyNoMoreInteractions(customerClient);
        verifyNoInteractions(catalogClient, inventoryClient, orderRepository, orderMapper, orderItemMapper, orderItemService, orderEventProducer);
    }

    @Test
    void createOrder_shouldCreateAndReturnOrderDto() {
        // Cliente válido
        when(customerClient.getActiveCustomerById(customerId)).thenReturn(customerDto);
        when(customerClient.getCustomerById(customerId)).thenReturn(customerDto);

        // Mapeos
        when(orderMapper.toEntity(orderRequestDto)).thenReturn(order);
        when(orderItemMapper.toEntity(orderItemRequestDto, order)).thenReturn(orderItem);
        //when(orderItemService.createOrderItem(orderItem)).thenReturn(orderItemDto);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        // Producto y stock
        when(catalogClient.getProductById(productId)).thenReturn(productDto);
        when(inventoryClient.hasSufficientStock(productId, orderItemRequestDto.getQty())).thenReturn(true);
        when(inventoryClient.reserveStock(productId, orderItemRequestDto.getQty())).thenReturn(stockReservedDto);

        // Guardado
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        OrderDto result = orderService.createOrder(orderRequestDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderId);
        verify(orderRepository).save(order);
        verify(orderEventProducer).emisorCompletedEvent(any(OrderCompletedEventDto.class));
    }

    @Test
    void createOrder_shouldThrowWhenStockInsufficient() {
        // Cliente válido
        when(customerClient.getActiveCustomerById(customerId)).thenReturn(customerDto);
        when(customerClient.getCustomerById(customerId)).thenReturn(customerDto);
        // El mapeo debe devolver un Order válido para evitar NPE
        when(orderMapper.toEntity(orderRequestDto)).thenReturn(order);
        // Producto activo pero sin stock suficiente
        when(catalogClient.getProductById(productId)).thenReturn(productDto);
        when(inventoryClient.hasSufficientStock(productId, orderItemRequestDto.getQty())).thenReturn(false);

        // Act + Assert
        assertThrows(StockInsufficientException.class, () -> orderService.createOrder(orderRequestDto));

        verify(inventoryClient).hasSufficientStock(productId, orderItemRequestDto.getQty());
        verify(inventoryClient, never()).reserveStock(anyLong(), anyInt());
        verify(orderRepository, never()).save(any());
        verify(orderEventProducer, never()).emisorCompletedEvent(any());
        // Verifica que no se mapean ni crean ítems
        verify(orderItemMapper, never()).toEntity(any(), any());
        verify(orderItemService, never()).createOrderItem(any());
    }






}

