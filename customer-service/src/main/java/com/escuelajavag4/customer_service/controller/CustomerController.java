package com.escuelajavag4.customer_service.controller;

import com.escuelajavag4.customer_service.dto.CustomerDto;
import com.escuelajavag4.customer_service.dto.CustomerRequestDto;
import com.escuelajavag4.customer_service.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    // Listar todos
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findCustomerById(id));
    }

    // Buscar si está activo
    @GetMapping("/{id}/active")
    public ResponseEntity<CustomerDto> getActiveCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findCustomerActiveById(id));
    }

    // Buscar por email
    @GetMapping("/search")
    public ResponseEntity<CustomerDto> getCustomerByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.findCustomerByEmail(email));
    }

    // Ingresar nuevo
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerRequestDto requestDto) {
        CustomerDto created = customerService.createCustomer(requestDto);
        URI uri = URI.create("/api/customers/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }


    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDto requestDto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, requestDto));
    }

    // Borrar de la db
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    // Desactivar customer
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CustomerDto> deactivateCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.deactivateCustomer(id));
    }

    // Activar o reactivar
    @PatchMapping("/{id}/activate")
    public ResponseEntity<CustomerDto> activateCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.activateCustomer(id));
    }
}
