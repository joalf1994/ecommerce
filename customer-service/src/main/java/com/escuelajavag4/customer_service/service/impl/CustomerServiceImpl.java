package com.escuelajavag4.customer_service.service.impl;

import com.escuelajavag4.customer_service.dto.CustomerDto;
import com.escuelajavag4.customer_service.dto.CustomerRequestDto;
import com.escuelajavag4.customer_service.exception.NotFoundException;
import com.escuelajavag4.customer_service.mapper.ICustomerMapper;
import com.escuelajavag4.customer_service.model.Customer;
import com.escuelajavag4.customer_service.repository.ICustomerRepository;
import com.escuelajavag4.customer_service.service.ICustomerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService {
    private final ICustomerRepository customerRepository;
    private final ICustomerMapper customerMapper;

    public CustomerServiceImpl(ICustomerRepository customerRepository, ICustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<CustomerDto> findAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .toList();
    }

    @Override
    public CustomerDto findCustomerActiveById(Long id) {
        Customer customer = customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found or inactive"));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDto findCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDto findCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Customer with email " + email + " not found"));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDto createCustomer(CustomerRequestDto requestDto) {
        Customer customer = customerMapper.toEntity(requestDto);
        customer.setActive(true);
        customer.setCreatedAt(LocalDateTime.now());
        return customerMapper.toDto(customerRepository.save(customer));
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerRequestDto requestDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));

        if (Boolean.FALSE.equals(customer.getActive())) {
            throw new IllegalStateException("Customer with id " + id + " is inactive and cannot be updated");
        }

        customer.setEmail(requestDto.getEmail());
        customer.setFullName(requestDto.getFullName());
        customer.setLevel(requestDto.getLevel());

        return customerMapper.toDto(customerRepository.save(customer));
    }


    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("Customer with id " + id + " not found");
        }
        customerRepository.deleteById(id);
    }

    @Transactional
    @Override
    public CustomerDto deactivateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));

        if (!customer.getActive()) {
            throw new IllegalStateException("Customer with id " + id + " is already inactive");
        }

        customer.setActive(false);
        return customerMapper.toDto(customer);
    }

    @Transactional
    @Override
    public CustomerDto activateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));

        if (Boolean.TRUE.equals(customer.getActive())) {
            throw new IllegalStateException("Customer with id " + id + " is already active");
        }

        customer.setActive(true);
        return customerMapper.toDto(customer);
    }
}
