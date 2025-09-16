package com.escuelajavag4.customer_service.service.impl;

import com.escuelajavag4.customer_service.dto.CustomerDto;
import com.escuelajavag4.customer_service.dto.CustomerRequestDto;
import com.escuelajavag4.customer_service.exception.NotFoundException;
import com.escuelajavag4.customer_service.mapper.ICustomerMapper;
import com.escuelajavag4.customer_service.model.Customer;
import com.escuelajavag4.customer_service.repository.ICustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    private CustomerRequestDto requestDto;

    @BeforeEach
    void setUp() {
        id = 1L;
        email = "rabines@gmail.com";

        customer = new Customer();
        customer.setId(id);
        customer.setEmail(email);
        customer.setFullName("Julio Rabines");
        customer.setLevel("BASIC");
        customer.setActive(true);
        customer.setCreatedAt(LocalDateTime.now());

        dto = new CustomerDto();
        dto.setId(id);
        dto.setEmail(email);
        dto.setFullName("Julio Rabines");
        dto.setLevel("BASIC");

        requestDto = new CustomerRequestDto();
        requestDto.setEmail(email);
        requestDto.setFullName("Julio Rabines");
        requestDto.setLevel("BASIC");
    }

    @Test
    void findAllCustomers_shouldReturnList() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(dto);

        List<CustomerDto> response = customerService.findAllCustomers();

        assertThat(response).isNotEmpty();
        assertThat(response.getFirst().getId()).isEqualTo(id);

        verify(customerRepository).findAll();
        verify(customerMapper).toDto(customer);
    }

    @Test
    void findAllCustomers_shouldReturnEmptyList() {
        when(customerRepository.findAll()).thenReturn(List.of());

        List<CustomerDto> response = customerService.findAllCustomers();

        assertThat(response).isEmpty();

        verify(customerRepository).findAll();
        verify(customerMapper, never()).toDto(any());
    }

    @Test
    void findCustomerById_shouldReturnCustomer() {
        when(customerRepository.findById(id))
                .thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer))
                .thenReturn(dto);
        CustomerDto response  = customerService.findCustomerById(id);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);

        verify(customerRepository).findById(id);
        verify(customerMapper).toDto(customer);
    }

    @Test
    void findCustomerById_shouldThrowExceptionWhenNotFound() {
        when(customerRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findCustomerById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with id 99 not found");

        verify(customerRepository).findById(99L);
        verify(customerMapper, never()).toDto(any());
    }

    @Test
    void findCustomerActiveById_shouldReturnActiveCustomer() {
        when(customerRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(dto);

        CustomerDto response = customerService.findCustomerActiveById(id);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
    }

    @Test
    void findCustomerActiveById_shouldThrowWhenNotFoundOrInactive() {
        when(customerRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findCustomerActiveById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with id " + id + " not found or inactive");
    }

    @Test
    void findCustomerByEmail_shouldReturnCustomer() {
        when(customerRepository.findByEmail(email))
               .thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer))
                .thenReturn(dto);

        CustomerDto response  = customerService.findCustomerByEmail(email);

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

        assertThatThrownBy(() -> customerService.findCustomerByEmail(emailNotFound))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with email " + emailNotFound + " not found");

        verify(customerRepository).findByEmail(emailNotFound);
        verify(customerMapper, never()).toDto(any());
    }

    @Test
    void createCustomer_shouldSaveAndReturnDto() {
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(dto);

        CustomerDto response = customerService.createCustomer(requestDto);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(email);

        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomer_shouldUpdateAndReturnDto() {
        CustomerRequestDto requestDtoAux = new CustomerRequestDto();
        requestDtoAux.setEmail("new@gmail.com");
        requestDtoAux.setFullName("Piero");
        requestDtoAux.setLevel("PREMIUM");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(dto);

        CustomerDto response = customerService.updateCustomer(id, requestDtoAux);

        assertThat(response).isNotNull();
        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomer_shouldThrowWhenNotFound() {
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.updateCustomer(id, new CustomerRequestDto()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with id " + id + " not found");
    }

    @Test
    void updateCustomer_shouldThrowWhenInactive() {
        customer.setActive(false);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> customerService.updateCustomer(id, new CustomerRequestDto()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Customer with id " + id + " is inactive and cannot be updated");
    }

    @Test
    void deleteCustomer_shouldDeleteWhenExists() {
        when(customerRepository.existsById(id)).thenReturn(true);

        customerService.deleteCustomer(id);

        verify(customerRepository).deleteById(id);
    }

    @Test
    void deleteCustomer_shouldThrowWhenNotFound() {
        when(customerRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> customerService.deleteCustomer(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with id " + id + " not found");
    }

    @Test
    void deactivateCustomer_shouldDeactivate() {
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(dto);

        CustomerDto response = customerService.deactivateCustomer(id);

        assertThat(response).isNotNull();
        assertThat(customer.getActive()).isFalse();
    }

    @Test
    void deactivateCustomer_shouldThrowWhenNotFound() {
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.deactivateCustomer(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with id " + id + " not found");
    }

    @Test
    void deactivateCustomer_shouldThrowWhenAlreadyInactive() {
        customer.setActive(false);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> customerService.deactivateCustomer(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Customer with id " + id + " is already inactive");
    }

    @Test
    void activateCustomer_shouldActivate() {
        customer.setActive(false);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(dto);

        CustomerDto response = customerService.activateCustomer(id);

        assertThat(response).isNotNull();
        assertThat(customer.getActive()).isTrue();
    }

    @Test
    void activateCustomer_shouldThrowWhenNotFound() {
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.activateCustomer(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Customer with id " + id + " not found");
    }

    @Test
    void activateCustomer_shouldThrowWhenAlreadyActive() {
        customer.setActive(true);
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> customerService.activateCustomer(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Customer with id " + id + " is already active");
    }
}