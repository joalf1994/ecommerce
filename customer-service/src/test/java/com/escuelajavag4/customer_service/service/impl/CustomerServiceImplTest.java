package com.escuelajavag4.customer_service.service.impl;

import com.escuelajavag4.customer_service.dto.CustomerDto;
import com.escuelajavag4.customer_service.exception.NotFoundException;
import com.escuelajavag4.customer_service.mapper.ICustomerMapper;
import com.escuelajavag4.customer_service.model.Customer;
import com.escuelajavag4.customer_service.repository.ICustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class CustomerServiceImplTest {

    @Mock
    private ICustomerRepository customerRepository;

    @Mock
    private ICustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Long id;
    private String email;
    private CustomerDto dto;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = 1L;
        email = "rabines@gmail.com";

        customer = new Customer();
        customer.setId(id);
        customer.setEmail(email);
        customer.setFullName("Julio Rabines");
        customer.setLevel("BASIC");
        customer.setCreatedAt(null);

        dto = new CustomerDto();
        dto.setId(id);
        dto.setEmail(email);
        dto.setFullName("Julio Rabines");
        dto.setLevel("BASIC");
    }

    @Test
    void findCustomerById_shouldReturnCustomer() {
        when(customerRepository.findById(id))
                .thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer))
                .thenReturn(dto);
        CustomerDto response  = customerService.findById(id);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);

        verify(customerRepository).findById(id);
        verify(customerMapper).toDto(customer);
    }

    @Test
    void findCustomerById_shouldThrowExceptionWhenNotFound() {
        when(customerRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with id 99 not found");

        verify(customerRepository).findById(99L);
        verify(customerMapper, never()).toDto(any());
    }

    @Test
    void findCustomerByEmail_shouldReturnCustomer() {
        when(customerRepository.findByEmail(email))
               .thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer))
                .thenReturn(dto);

        CustomerDto response  = customerService.findByEmail(email);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(email);

        verify(customerRepository).findByEmail(email);
        verify(customerMapper).toDto(customer);
    }

    @Test
    void findCustomerByEmail_shouldThrowExceptionWhenNotFound() {
        String emailNotFound = "socorro90@gmail.com";
        when(customerRepository.findByEmail(emailNotFound))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findByEmail(emailNotFound))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with email " + emailNotFound + " not found");

        verify(customerRepository).findByEmail(emailNotFound);
        verify(customerMapper, never()).toDto(any());
    }
}