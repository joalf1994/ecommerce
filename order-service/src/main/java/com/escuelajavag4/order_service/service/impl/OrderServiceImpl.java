package com.escuelajavag4.order_service.service.impl;

import com.escuelajavag4.order_service.dto.OrderDto;
import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.dto.OrderItemRequestDto;
import com.escuelajavag4.order_service.dto.OrderRequestDto;
import com.escuelajavag4.order_service.dto.customer.CustomerDto;
import com.escuelajavag4.order_service.dto.inventory.StockReservedDto;
import com.escuelajavag4.order_service.dto.kafka.OrderCompletedEventDto;
import com.escuelajavag4.order_service.dto.product.ProductDto;
import com.escuelajavag4.order_service.exception.CustomerNotFoundException;
import com.escuelajavag4.order_service.exception.OrderNotFoundException;
import com.escuelajavag4.order_service.exception.ProductNotFoundException;
import com.escuelajavag4.order_service.exception.StockInsufficientException;
import com.escuelajavag4.order_service.feign.CatalogClient;
import com.escuelajavag4.order_service.feign.CustomerClient;
import com.escuelajavag4.order_service.feign.InventoryClient;
import com.escuelajavag4.order_service.mapper.IOrderItemMapper;
import com.escuelajavag4.order_service.mapper.IOrderMapper;
import com.escuelajavag4.order_service.messaging.OrderEventProducer;
import com.escuelajavag4.order_service.model.Order;
import com.escuelajavag4.order_service.model.OrderItem;
import com.escuelajavag4.order_service.repository.IOrderRepository;
import com.escuelajavag4.order_service.service.IOrderItemService;
import com.escuelajavag4.order_service.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderMapper orderMapper;
    private final IOrderItemMapper orderItemMapper;
    private final IOrderItemService orderItemService;

    private final CustomerClient customerClient;
    private final CatalogClient catalogClient;
    private final InventoryClient inventoryClient;

    private final OrderEventProducer orderEventProducer;

    @Override
    public OrderDto findOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + id + " not found"));
        OrderDto orderDto = orderMapper.toDto(order);

        // Validación defensiva: si la lista es null, usar lista vacía
        List<OrderItemDto> orderItemDtos = ((order.getItems() != null ? order.getItems() : List.<OrderItem>of()))
                .stream()
                .map(orderItemMapper::toDto)
                .toList();

        orderDto.setItems(orderItemDtos);
        // Si la lista sigue vacía, revisar que los OrderItem realmente tengan la referencia a la Order y estén guardados correctamente
        return orderDto;
    }

    @Override
    public OrderDto createOrder(OrderRequestDto request) {
        validateCustomer(request.getCustomerId());
        String email = customerClient.getCustomerById(request.getCustomerId()).getEmail();

        Order order = orderMapper.toEntity(request);
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        validateActiveProductAndStock(request.getItems());

        List<OrderItem> items = request.getItems().stream()
                .map(dto -> {
                    ProductDto product = catalogClient.getProductById(dto.getProductId());
                    inventoryClient.reserveStock(dto.getProductId(), dto.getQty());

                    OrderItem item = orderItemMapper.toEntity(dto, order);
                    item.setUnitPrice(product.getPrice());
                    BigDecimal subTotal = product.getPrice().multiply(BigDecimal.valueOf(dto.getQty()));
                    item.setSubtotal(subTotal);

                    return item;
                })
                .toList();

        order.setItems(items);

        BigDecimal amount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(amount.doubleValue());

        Order savedOrder = orderRepository.save(order);

        OrderCompletedEventDto orderCompletedEventDto = new OrderCompletedEventDto();
        orderCompletedEventDto.setOrderId(savedOrder.getId()); // aquí sí tienes el id
        orderCompletedEventDto.setAmount(amount);
        orderCompletedEventDto.setEmail(email);

        orderEventProducer.emisorCompletedEvent(orderCompletedEventDto);

        return orderMapper.toDto(savedOrder);
    }


    private void validateCustomer(Long customerId) {
        CustomerDto customer = customerClient.getActiveCustomerById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
        }
    }

    private void validateActiveProductAndStock(List<OrderItemRequestDto> items) {
        for (OrderItemRequestDto item : items) {
            Long productId = item.getProductId();
            Integer qty = item.getQty();

            ProductDto product = catalogClient.getProductById(productId);
            if (product == null || Boolean.FALSE.equals(product.getActive())) {
                throw new ProductNotFoundException("Product " + item.getProductId() + " not available");
            }
            
            if (qty == null || qty <= 0) {
                throw new StockInsufficientException("La cantidad debe ser mayor a cero para el producto " + productId);
            }

            boolean disponible = inventoryClient.hasSufficientStock(productId, qty);
            if (!disponible) {
                throw new StockInsufficientException("No hay suficiente stock para el producto " + item.getProductId());
            }
        }
    }
}

















