package com.escuelajavag4.catalogservice.repository;

import com.escuelajavag4.catalogservice.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
