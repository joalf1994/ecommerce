package com.escuelajavag4.order_service.feign;

import com.escuelajavag4.order_service.dto.customer.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerClient {
    @GetMapping("/api/customers/{id}/active")
    CustomerDto getActiveCustomerById(@PathVariable Long id);

    @GetMapping("/api/customers/{id}")
    CustomerDto getCustomerById(@PathVariable Long id);
}
