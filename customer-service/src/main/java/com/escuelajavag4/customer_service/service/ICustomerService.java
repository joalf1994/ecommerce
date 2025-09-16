package com.escuelajavag4.customer_service.service;

import com.escuelajavag4.customer_service.dto.CustomerDto;
import com.escuelajavag4.customer_service.dto.CustomerRequestDto;

import java.util.List;

public interface ICustomerService {
    List<CustomerDto> findAllCustomers();
    CustomerDto findCustomerActiveById(Long id);
    CustomerDto findCustomerById(Long id);
    CustomerDto findCustomerByEmail(String email);
    CustomerDto createCustomer(CustomerRequestDto requestDto);
    CustomerDto updateCustomer(Long id, CustomerRequestDto requestDto);
    void deleteCustomer(Long id);
    CustomerDto deactivateCustomer(Long id);
    CustomerDto activateCustomer(Long id);
}
