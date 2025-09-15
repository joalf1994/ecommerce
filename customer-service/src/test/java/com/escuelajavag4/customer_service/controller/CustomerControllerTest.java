package com.escuelajavag4.customer_service.controller;

import com.escuelajavag4.customer_service.dto.CustomerDto;
import com.escuelajavag4.customer_service.exception.NotFoundException;
import com.escuelajavag4.customer_service.service.ICustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private ICustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private Long id;
    private String email;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        id = 1L;
        email = "pedro23@gmail.com";

        customerDto = new CustomerDto();
        customerDto.setId(id);
        customerDto.setEmail(email);
        customerDto.setFullName("Pedro Alva Díaz");
        customerDto.setLevel("BASIC");
    }

    @Test
    void getCustomerById_shouldReturnCustomer() {
        when(customerService.findById(id)).thenReturn(customerDto);

        CustomerDto response = customerController.getCustomerById(id);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);

        verify(customerService).findById(id);
    }

    @Test
    void getCustomerById_shouldThrowExceptionWhenNotFound() {
        when(customerService.findById(99L))
                .thenThrow(new NotFoundException("Customer not found"));

        assertThatThrownBy(() -> customerController.getCustomerById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");

        verify(customerService).findById(99L);
    }

    @Test
    void getCustomerByEmail_shouldReturnCustomer() {
        when(customerService.findByEmail(email)).thenReturn(customerDto);
        CustomerDto response = customerController.getCustomerByEmail(email);
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(email);
        verify(customerService).findByEmail(email);
    }

    @Test
    void getCustomerByEmail_shouldThrowExceptionWhenNotFound() {
        when(customerService.findByEmail(email))
                .thenThrow(new NotFoundException("Customer with email " + email + " not found"));

        assertThatThrownBy(() -> customerController.getCustomerByEmail(email))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with email " + email + " not found");

        verify(customerService).findByEmail(email);

    }
}