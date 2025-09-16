package com.escuelajavag4.paymentservice.controller;

import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentResponseDto;
import com.escuelajavag4.paymentservice.model.dto.PaymentUpdateRequestDto;
import com.escuelajavag4.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> create(@RequestBody PaymentCreateRequestDto dto) {
        PaymentResponseDto response = paymentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponseDto> updateStatus(
            @PathVariable Long paymentId,
            @RequestBody PaymentUpdateRequestDto dto
    ) {
        PaymentResponseDto response = paymentService.updateStatus(paymentId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> findById(@PathVariable Long paymentId) {
        PaymentResponseDto response = paymentService.findById(paymentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDto> findByOrderId(@PathVariable Long orderId) {
        PaymentResponseDto response = paymentService.findByOrderId(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> findAll() {
        List<PaymentResponseDto> response = paymentService.findAll();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> delete(@PathVariable Long paymentId) {
        paymentService.delete(paymentId);
        return ResponseEntity.noContent().build();
    }
}