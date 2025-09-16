package com.escuelajavag4.customer_service.controller;

import com.escuelajavag4.customer_service.dto.CustomerDto;
import com.escuelajavag4.customer_service.dto.CustomerRequestDto;
import com.escuelajavag4.customer_service.exception.NotFoundException;
import com.escuelajavag4.customer_service.service.ICustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
    private CustomerRequestDto requestDto;

    @BeforeEach
    void setUp() {
        id = 1L;
        email = "pedro23@gmail.com";

        customerDto = new CustomerDto();
        customerDto.setId(id);
        customerDto.setEmail(email);
        customerDto.setFullName("Pedro Alva Díaz");
        customerDto.setLevel("BASIC");
        customerDto.setActive(true);

        requestDto = new CustomerRequestDto();
        requestDto.setEmail("carlose@test.com");
        requestDto.setFullName("Carlos Villalobos");
        requestDto.setLevel("PREMIUM");
    }

    @Test
    void getAllCustomers_shouldReturnList() {
        when(customerService.findAllCustomers()).thenReturn(List.of(customerDto));

        ResponseEntity<List<CustomerDto>> response = customerController.getAllCustomers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody().getFirst().getId()).isEqualTo(id);
        verify(customerService).findAllCustomers();
    }

    @Test
    void getAllCustomers_shouldThrowExceptionWhenServiceFails() {
        when(customerService.findAllCustomers())
                .thenThrow(new RuntimeException("Database connection failed"));

        assertThatThrownBy(() -> customerController.getAllCustomers())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database connection failed");

        verify(customerService).findAllCustomers();
    }

    @Test
    void getCustomerById_shouldReturnCustomer() {
        when(customerService.findCustomerById(id)).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerController.getCustomerById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);

        verify(customerService).findCustomerById(id);
    }

    @Test
    void getCustomerById_shouldThrowExceptionWhenNotFound() {
        when(customerService.findCustomerById(99L))
                .thenThrow(new NotFoundException("Customer not found"));

        assertThatThrownBy(() -> customerController.getCustomerById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");

        verify(customerService).findCustomerById(99L);
    }

    @Test
    void getCustomerByEmail_shouldReturnCustomer() {
        when(customerService.findCustomerByEmail(email)).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerController.getCustomerByEmail(email);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(email);

        verify(customerService).findCustomerByEmail(email);
    }

    @Test
    void getCustomerByEmail_shouldThrowExceptionWhenNotFound() {
        when(customerService.findCustomerByEmail(email))
                .thenThrow(new NotFoundException("Customer with email " + email + " not found"));

        assertThatThrownBy(() -> customerController.getCustomerByEmail(email))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with email " + email + " not found");

        verify(customerService).findCustomerByEmail(email);
    }

    @Test
    void getActiveCustomerById_shouldReturnActiveCustomer() {
        when(customerService.findCustomerActiveById(id)).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerController.getActiveCustomerById(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getActive()).isTrue();

        verify(customerService).findCustomerActiveById(id);
    }

    @Test
    void getActiveCustomerById_shouldThrowExceptionWhenInactive() {
        when(customerService.findCustomerActiveById(id))
                .thenThrow(new NotFoundException("Customer not found or inactive"));

        assertThatThrownBy(() -> customerController.getActiveCustomerById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found or inactive");

        verify(customerService).findCustomerActiveById(id);
    }

    @Test
    void createCustomer_shouldReturnNewCustomer() {
        when(customerService.createCustomer(requestDto)).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerController.createCustomer(requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation().toString())
                .isEqualTo("/api/customers/" + id);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(email);

        verify(customerService).createCustomer(requestDto);
    }

    @Test
    void createCustomer_shouldThrowExceptionWhenInvalid() {
        when(customerService.createCustomer(requestDto))
                .thenThrow(new IllegalArgumentException("Invalid customer data"));

        assertThatThrownBy(() -> customerController.createCustomer(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid customer data");

        verify(customerService).createCustomer(requestDto);
    }

    @Test
    void updateCustomer_shouldReturnUpdatedCustomer() {
        when(customerService.updateCustomer(id, requestDto)).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerController.updateCustomer(id, requestDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);

        verify(customerService).updateCustomer(id, requestDto);
    }

    @Test
    void updateCustomer_shouldThrowExceptionWhenNotFound() {
        when(customerService.updateCustomer(id, requestDto))
                .thenThrow(new NotFoundException("Customer not found"));

        assertThatThrownBy(() -> customerController.updateCustomer(id, requestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");

        verify(customerService).updateCustomer(id, requestDto);
    }

    @Test
    void deleteCustomer_shouldCallService() {
        doNothing().when(customerService).deleteCustomer(id);

        ResponseEntity<Void> response = customerController.deleteCustomer(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(customerService).deleteCustomer(id);
    }

    @Test
    void deleteCustomer_shouldThrowExceptionWhenNotFound() {
        doThrow(new NotFoundException("Customer not found"))
                .when(customerService).deleteCustomer(id);

        assertThatThrownBy(() -> customerController.deleteCustomer(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer not found");

        verify(customerService).deleteCustomer(id);
    }

    @Test
    void deactivateCustomer_shouldReturnDeactivatedCustomer() {
        customerDto.setActive(false);
        when(customerService.deactivateCustomer(id)).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerController.deactivateCustomer(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getActive()).isFalse();

        verify(customerService).deactivateCustomer(id);
    }

    @Test
    void deactivateCustomer_shouldThrowExceptionWhenAlreadyInactive() {
        when(customerService.deactivateCustomer(id))
                .thenThrow(new IllegalStateException("Customer is already inactive"));

        assertThatThrownBy(() -> customerController.deactivateCustomer(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Customer is already inactive");

        verify(customerService).deactivateCustomer(id);
    }

    @Test
    void activateCustomer_shouldReturnActivatedCustomer() {
        customerDto.setActive(true);
        when(customerService.activateCustomer(id)).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerController.activateCustomer(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getActive()).isTrue();

        verify(customerService).activateCustomer(id);
    }

    @Test
    void activateCustomer_shouldThrowExceptionWhenAlreadyActive() {
        when(customerService.activateCustomer(id))
                .thenThrow(new IllegalStateException("Customer is already active"));

        assertThatThrownBy(() -> customerController.activateCustomer(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Customer is already active");

        verify(customerService).activateCustomer(id);
    }
}