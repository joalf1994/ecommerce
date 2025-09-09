package com.escuelajavag4.catalogservice.repository;

import com.escuelajavag4.catalogservice.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    /**
     * Buscar categoría por nombre (ignorando mayúsculas/minúsculas)
     */
    Optional<Category> findByNameIgnoreCase(String name);

    /**
     * Verificar si existe una categoría con el nombre dado (ignorando mayúsculas/minúsculas)
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Obtener todas las categorías activas
     */
    List<Category> findByActiveTrue();

    /**
     * Obtener todas las categorías inactivas
     */
    List<Category> findByActiveFalse();

    /**
     * Buscar categorías por nombre que contenga el texto dado
     */
    List<Category> findByNameContainingIgnoreCase(String name);

    /**
     * Buscar categorías activas por nombre que contenga el texto dado
     */
    List<Category> findByNameContainingIgnoreCaseAndActiveTrue(String name);
}
