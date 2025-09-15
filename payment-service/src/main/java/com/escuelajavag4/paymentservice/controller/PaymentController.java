package com.escuelajavag4.paymentservice.controller;

import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentResponseDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentUpdateRequestDto;
import com.escuelajavag4.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponseDto create(@RequestBody PaymentCreateRequestDto dto) {
        return paymentService.create(dto);
    }

    @PatchMapping("/{paymentId}/status")
    public PaymentResponseDto updateStatus(
            @PathVariable Long paymentId,
            @RequestBody PaymentUpdateRequestDto dto
    ) {
        return paymentService.updateStatus(paymentId, dto);
    }

    @GetMapping("/{paymentId}")
    public PaymentResponseDto findById(@PathVariable Long paymentId) {
        return paymentService.findById(paymentId);
    }

    @GetMapping("/order/{orderId}")
    public PaymentResponseDto findByOrderId(@PathVariable Long orderId) {
        return paymentService.findByOrderId(orderId);
    }

    @GetMapping
    public List<PaymentResponseDto> findAll() {
        return paymentService.findAll();
    }

    @DeleteMapping("/{paymentId}")
    public void delete(@PathVariable Long paymentId) {
        paymentService.delete(paymentId);
    }
}