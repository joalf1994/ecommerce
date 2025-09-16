package com.escuelajavag4.order_service.service.impl;

import com.escuelajavag4.order_service.dto.OrderDto;
import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.dto.OrderItemRequestDto;
import com.escuelajavag4.order_service.dto.OrderRequestDto;
import com.escuelajavag4.order_service.dto.customer.CustomerDto;
import com.escuelajavag4.order_service.dto.inventory.StockDto;
import com.escuelajavag4.order_service.dto.product.ProductDto;
import com.escuelajavag4.order_service.exception.CustomerNotFoundException;
import com.escuelajavag4.order_service.exception.ProductNotFoundException;
import com.escuelajavag4.order_service.exception.StockInsufficientException;
import com.escuelajavag4.order_service.feign.CatalogClient;
import com.escuelajavag4.order_service.feign.CustomerClient;
import com.escuelajavag4.order_service.feign.InventoryClient;
import com.escuelajavag4.order_service.feign.PaymentClient;
import com.escuelajavag4.order_service.mapper.IOrderItemMapper;
import com.escuelajavag4.order_service.mapper.IOrderMapper;
import com.escuelajavag4.order_service.model.Order;
import com.escuelajavag4.order_service.model.OrderItem;
import com.escuelajavag4.order_service.model.PaymentMethod;
import com.escuelajavag4.order_service.repository.IOrderRepository;
import com.escuelajavag4.order_service.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderMapper orderMapper;
    private final IOrderItemMapper orderItemMapper;

    private final CustomerClient customerClient;
    private final CatalogClient catalogClient;
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;


    //Request
//    {
//        "customerId": "c123",
//            "items": [
//        {"productId":"p100","qty":2},
//        {"productId":"p20","qty":1}
//  ],
//        "paymentMethod": "ghnmgh"
//    }


    //Response
//    {
//        "orderId": "o-10045",
//            "status": "CREATED",
//            "total": 269.70,
//            "items": [
//        {"productId":"p10","qty":2,"unitPrice":89.9,"subtotal":179.8},
//        {"productId":"p20","qty":1,"unitPrice":89.9,"subtotal":89.9}
//  ]
//    }


    @Override
    public OrderDto findAllOrders() {
        return null;
    }

    @Override
    public OrderDto findOrderById(Long id) {
        return null;
    }

    @Override
    public OrderDto createOrder(OrderRequestDto request) {
        // validamos el cliente
        validateCustomer(request.getCustomerId());

        Order order = orderMapper.toEntity(request);
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        // validamos productos y stock
        validateActiveProductAndStock(request.getItems());

        // Mapeamos los items
        List<OrderItem> items = request.getItems().stream()
                .map(dto ->  {
                    ProductDto product = catalogClient.getProductById(dto.getProductId());
                    StockDto stock = inventoryClient.getStockByProductId(dto.getProductId());

                    // reservamos stock, actualizamos el service inventory
                    inventoryClient.reserveStock(dto.getProductId(), dto.getQty());

                    //creamos el item
                    OrderItem item = orderItemMapper.toEntity(dto, order);
                    item.setUnitPrice(product.getPrice());
                    BigDecimal subTotal = product.getPrice().multiply(BigDecimal.valueOf(dto.getQty()));
                    item.setSubtotal(subTotal);
                    return item;
                })
                .toList();

        order.setItems(items);

        // Calculamos el total
        BigDecimal total = items.stream()
                        .map(OrderItem::getSubtotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total.doubleValue());

        return orderMapper.toDto(orderRepository.save(order));
    }


    //Llamar a customer-service vía Feign Client → si el cliente no existe o está inactivo → rechazar orden.
    private void validateCustomer(Long customerId) {
        try {
            customerClient.getCustomerById(customerId);
        } catch (feign.FeignException.NotFound e) {
            throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
        }
    }

    private void validateActiveProductAndStock(List<OrderItemRequestDto> items) {
        for (OrderItemRequestDto item : items) {
            ProductDto product = catalogClient.getProductById(item.getProductId());
            //Producto existe y está activo → se continúa.
            //Producto no existe → lanzar excepción (ProductNotFoundException) y rechazar la orden.
            //Producto existe pero está inactivo (ej. descontinuado) → no se puede vender → rechazar.
            if (product == null || !product.getActive()) {
                throw new ProductNotFoundException("Producto " + item.getProductId() + " no disponible");
            }
            StockDto stock = inventoryClient.getStockByProductId(item.getProductId());
//            Consulta el servicio de inventory para obtener los detalles de stock de un producto
//            verifica si la cantidad deseada está disponible.
//            Lanza una excepción en tiempo de ejecución si no hay stock suficiente.
            if (stock.getAvailable() < item.getQty()) {
                throw new StockInsufficientException("Stock insuficiente para producto " + item.getProductId());
            }
        }
    }

}


//
//{
//        "status": "ERROR",
//        "message": "Producto con ID p20 no encontrado",
//        "invalidProductId": "p20"
//        }
//        📌 Ejemplo si pasa la validación:

//json
//Copiar código
//{
//        "orderId": "o789",
//        "status": "CONFIRMED",
//        "totalAmount": 120.50
//        }


















