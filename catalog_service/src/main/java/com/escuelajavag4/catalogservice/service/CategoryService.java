package com.escuelajavag4.catalogservice.service;

import com.escuelajavag4.catalogservice.model.dto.request.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.CategoryResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.CategoryUpdateRequestDto;

import java.util.List;

public interface CategoryService {

    /**
     * Crear una nueva categoría
     */
    CategoryResponseDto createCategory(CategoryCreateRequestDto createRequestDto);

    /**
     * Obtener todas las categorías
     */
    List<CategoryResponseDto> getAllCategories();

    /**
     * Obtener categoría por ID
     */
    CategoryResponseDto getCategoryById(Long id);

    /**
     * Obtener categoría por ID con productos
     */
    CategoryResponseDto getCategoryByIdWithProducts(Long id);

    /**
     * Obtener categoría por nombre
     */
    CategoryResponseDto getCategoryByName(String name);

    /**
     * Obtener todas las categorías activas
     */
    List<CategoryResponseDto> getActiveCategories();

    /**
     * Actualizar categoría
     */
    CategoryResponseDto updateCategory(Long id, CategoryUpdateRequestDto updateRequestDto);

    /**
     * Eliminar categoría (eliminación lógica - marcar como inactiva)
     */
    void deactivateCategory(Long id);

    /**
     * Activar categoría
     */
    CategoryResponseDto activateCategory(Long id);

    /**
     * Eliminar categoría físicamente
     */
    void deleteCategory(Long id);

}
