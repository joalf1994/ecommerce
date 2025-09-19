package com.escuelajavag4.catalogservice.repository;

import com.escuelajavag4.catalogservice.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    List<Product> findByActiveTrue();

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByMarcaId(Long marcaId);

    List<Product> findByNameContainingIgnoreCase(String name);

}
