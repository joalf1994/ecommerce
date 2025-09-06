package com.escuelajavag4.customer_service.repository;

import com.escuelajavag4.customer_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);
}
