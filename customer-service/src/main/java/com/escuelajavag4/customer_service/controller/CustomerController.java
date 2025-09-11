package com.escuelajavag4.customer_service.controller;

import com.escuelajavag4.customer_service.dto.CustomerDto;
import com.escuelajavag4.customer_service.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<CustomerDto> getCustomerByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.findByEmail(email));
    }
}
