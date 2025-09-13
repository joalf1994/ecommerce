package com.escuelajavag4.catalogservice.service;


import com.escuelajavag4.catalogservice.exception.DuplicateResourceException;
import com.escuelajavag4.catalogservice.exception.ResourceHasDependenciesException;
import com.escuelajavag4.catalogservice.exception.ResourceNotFoundException;
import com.escuelajavag4.catalogservice.mapper.CategoryMapper;
import com.escuelajavag4.catalogservice.model.dto.request.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.request.CategoryUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.CategoryResponseDto;
import com.escuelajavag4.catalogservice.model.entity.Category;
import com.escuelajavag4.catalogservice.model.entity.Product;
import com.escuelajavag4.catalogservice.repository.CategoryRepository;
import com.escuelajavag4.catalogservice.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryResponseDto categoryDto;

    @BeforeEach
    void init() {
        category = new Category();
        category.setId(1L);
        category.setName("Bebidas");
        category.setActive(true);

        categoryDto = new CategoryResponseDto();
        categoryDto.setId(1L);
        categoryDto.setName("Bebidas");
        categoryDto.setActive(true);
    }

    @Test
    void crear_ok() {
        CategoryCreateRequestDto req = new CategoryCreateRequestDto();
        req.setName("Bebidas");

        when(categoryRepository.existsByNameIgnoreCase("Bebidas")).thenReturn(false);
        when(categoryMapper.toEntity(req)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryDto);

        CategoryResponseDto result = categoryService.createCategory(req);

        assertNotNull(result);
        assertEquals("Bebidas", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void crear_nombreDuplicado() {
        CategoryCreateRequestDto req = new CategoryCreateRequestDto();
        req.setName("Bebidas");

        when(categoryRepository.existsByNameIgnoreCase("Bebidas")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> categoryService.createCategory(req));
    }

    @Test
    void buscarPorId_ok() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryDto);

        CategoryResponseDto result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void buscarPorId_noExiste() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getCategoryById(99L));
    }

    @Test
    void actualizar_ok() {
        CategoryUpdateRequestDto req = new CategoryUpdateRequestDto();
        req.setName("Alimentos");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCase("Alimentos")).thenReturn(false);
        doNothing().when(categoryMapper).updateEntityFromDto(req, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryDto);

        CategoryResponseDto result = categoryService.updateCategory(1L, req);

        assertNotNull(result);
        verify(categoryRepository).save(category);
    }

    @Test
    void activar_ok() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryDto);

        CategoryResponseDto result = categoryService.activateCategory(1L);

        assertNotNull(result);
        assertTrue(result.getActive());
        verify(categoryRepository).save(category);
    }

    @Test
    void desactivar_ok() {
        category.setProducts(Collections.emptySet());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        assertDoesNotThrow(() -> categoryService.deactivateCategory(1L));
        verify(categoryRepository).save(category);
    }

    @Test
    void desactivar_conProductos() {
        category.setProducts(Set.of(new Product()));

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(ResourceHasDependenciesException.class,
                () -> categoryService.deactivateCategory(1L));
    }

    @Test
    void eliminar_ok() {
        category.setProducts(Collections.emptySet());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void eliminar_conProductos() {
        category.setProducts(Set.of(new Product()));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(ResourceHasDependenciesException.class,
                () -> categoryService.deleteCategory(1L));
    }

    @Test
    void listar_todas() {
        List<Category> lista = List.of(category);
        when(categoryRepository.findAll()).thenReturn(lista);
        when(categoryMapper.toDtoList(lista)).thenReturn(List.of(categoryDto));

        List<CategoryResponseDto> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void listar_activas() {
        List<Category> lista = List.of(category);
        when(categoryRepository.findByActiveTrue()).thenReturn(lista);
        when(categoryMapper.toDtoList(lista)).thenReturn(List.of(categoryDto));

        List<CategoryResponseDto> result = categoryService.getActiveCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}