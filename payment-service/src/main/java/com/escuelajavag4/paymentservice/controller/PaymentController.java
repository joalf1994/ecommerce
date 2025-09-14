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

    // ================== CREATE ==================
    @PostMapping
    public PaymentResponseDto create(@RequestBody PaymentCreateRequestDto dto) {
        return paymentService.create(dto);
    }

    // ================== UPDATE ==================
    @PatchMapping("/{paymentId}/status")
    public PaymentResponseDto updateStatus(
            @PathVariable Long paymentId,
            @RequestBody PaymentUpdateRequestDto dto
    ) {
        return paymentService.updateStatus(paymentId, dto);
    }

    // ================== GET BY ID ==================
    @GetMapping("/{paymentId}")
    public PaymentResponseDto findById(@PathVariable Long paymentId) {
        return paymentService.findById(paymentId);
    }

    // ================== GET BY ORDER ID ==================
    @GetMapping("/order/{orderId}")
    public PaymentResponseDto findByOrderId(@PathVariable Long orderId) {
        return paymentService.findByOrderId(orderId);
    }

    // ================== GET ALL ==================
    @GetMapping
    public List<PaymentResponseDto> findAll() {
        return paymentService.findAll();
    }

    // ================== DELETE ==================
    @DeleteMapping("/{paymentId}")
    public void delete(@PathVariable Long paymentId) {
        paymentService.delete(paymentId);
    }
}