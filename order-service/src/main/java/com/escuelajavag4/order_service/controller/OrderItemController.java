package com.escuelajavag4.order_service.controller;

import com.escuelajavag4.order_service.dto.OrderItemDto;
import com.escuelajavag4.order_service.dto.OrderItemRequestDto;
import com.escuelajavag4.order_service.service.IOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final IOrderItemService orderItemService;

    @GetMapping("/{productId}/all")
    public ResponseEntity<List<OrderItemDto>> orderItemsByProductId(@PathVariable Long productId) {
            List<OrderItemDto> orderItemDtos = orderItemService.orderItemsByProductId(productId);
        return ResponseEntity.ok(orderItemDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.findById(id));
    }

    @PostMapping
    public ResponseEntity<OrderItemDto> createOrderItem(@RequestBody OrderItemRequestDto request) {
        return ResponseEntity.ok(orderItemService.createOrderItem(request));
    }


}
