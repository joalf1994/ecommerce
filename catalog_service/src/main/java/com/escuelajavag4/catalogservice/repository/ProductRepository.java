package com.escuelajavag4.catalogservice.repository;

import com.escuelajavag4.catalogservice.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    // Buscar producto por código exacto (case insensitive)
    Optional<Product> findByCodeIgnoreCase(String code);

    // Verificar existencia de código único
    boolean existsByCodeIgnoreCase(String code);

    // Buscar productos activos
    List<Product> findByActiveTrue();

    // Buscar productos por categoría
    List<Product> findByCategoryId(Long categoryId);

    // Buscar productos activos por categoría
    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    // Buscar productos por marca
    List<Product> findByMarcaId(Long marcaId);

    // Buscar productos activos por marca
    List<Product> findByMarcaIdAndActiveTrue(Long marcaId);

    // Buscar productos por nombre (like / case insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Buscar productos por categoría y marca
    List<Product> findByCategoryIdAndMarcaId(Long categoryId, Long marcaId);

    // Buscar productos activos por categoría y marca
    List<Product> findByCategoryIdAndMarcaIdAndActiveTrue(Long categoryId, Long marcaId);
}
