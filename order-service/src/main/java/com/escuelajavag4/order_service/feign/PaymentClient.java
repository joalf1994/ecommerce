package com.escuelajavag4.order_service.feign;

import com.escuelajavag4.order_service.dto.payment.PaymentDto;
import com.escuelajavag4.order_service.dto.payment.PaymentRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/api/payments")
    PaymentDto create(@RequestBody PaymentRequestDto dto);
}



