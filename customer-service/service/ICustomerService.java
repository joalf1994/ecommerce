package com.escuelajavag4.customer_service.service;

import com.escuelajavag4.customer_service.dto.CustomerDto;

public interface ICustomerService {
    CustomerDto findById(Long id);
    CustomerDto findByEmail(String email);
}
