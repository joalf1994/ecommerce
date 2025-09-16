package com.escuelajavag4.order_service.controller;

import com.escuelajavag4.order_service.dto.OrderDto;
import com.escuelajavag4.order_service.dto.OrderRequestDto;
import com.escuelajavag4.order_service.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @GetMapping
    public OrderDto findAll() {
        return orderService.findAllOrders();
    }

    @RequestMapping("/{id}")
    public OrderDto findById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }

    @PostMapping("/create")
    public OrderDto createOrder(@RequestBody OrderRequestDto request) {
        return orderService.createOrder(request);
    }
}
