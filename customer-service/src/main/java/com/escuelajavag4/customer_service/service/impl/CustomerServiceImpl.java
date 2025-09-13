package com.escuelajavag4.customer_service.service.impl;

import com.escuelajavag4.customer_service.dto.CustomerDto;
import com.escuelajavag4.customer_service.exception.NotFoundException;
import com.escuelajavag4.customer_service.mapper.ICustomerMapper;
import com.escuelajavag4.customer_service.model.Customer;
import com.escuelajavag4.customer_service.repository.ICustomerRepository;
import com.escuelajavag4.customer_service.service.ICustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements ICustomerService {
    private final ICustomerRepository customerRepository;
    private final ICustomerMapper customerMapper;

    public CustomerServiceImpl(ICustomerRepository customerRepository, ICustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDto findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDto findByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Customer with email " + email + " not found"));
        return customerMapper.toDto(customer);
    }
}
