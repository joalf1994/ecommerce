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

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock private CategoryRepository categoryRepository;
    @Mock private CategoryMapper categoryMapper;

    @InjectMocks private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryResponseDto categoryDto;

    @BeforeEach
    void init() {
        category = new Category();
        category.setId(1L);
        category.setName("Electrónica");
        category.setActive(true);
        category.setProducts(new HashSet<>());

        categoryDto = new CategoryResponseDto();
        categoryDto.setId(1L);
        categoryDto.setName("Electrónica");
        categoryDto.setActive(true);
    }

    @Test
    void crear_ok() {
        CategoryCreateRequestDto req = new CategoryCreateRequestDto();
        req.setName("Electrónica");

        when(categoryRepository.existsByNameIgnoreCase("Electrónica")).thenReturn(false);
        when(categoryMapper.toEntity(req)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryDto);

        CategoryResponseDto result = categoryService.createCategory(req);

        assertEquals("Electrónica", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void crear_falla_nombreDuplicado() {
        CategoryCreateRequestDto req = new CategoryCreateRequestDto();
        req.setName("Electrónica");

        when(categoryRepository.existsByNameIgnoreCase("Electrónica")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> categoryService.createCategory(req));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getAll_ok() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toDtoList(anyList())).thenReturn(List.of(categoryDto));

        List<CategoryResponseDto> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
    }

    @Test
    void getById_ok() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryDto);

        CategoryResponseDto result = categoryService.getCategoryById(1L);

        assertEquals("Electrónica", result.getName());
    }

    @Test
    void getById_notFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getCategoryById(1L));
    }

    @Test
    void getByIdWithProducts_ok() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDtoWithProducts(category)).thenReturn(categoryDto);

        CategoryResponseDto result = categoryService.getCategoryByIdWithProducts(1L);

        assertEquals("Electrónica", result.getName());
    }

    @Test
    void getByIdWithProducts_notFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getCategoryByIdWithProducts(1L));
    }

    @Test
    void getByName_ok() {
        when(categoryRepository.findByNameIgnoreCase("Electrónica")).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryDto);

        CategoryResponseDto result = categoryService.getCategoryByName("Electrónica");

        assertEquals("Electrónica", result.getName());
    }

    @Test
    void getByName_notFound() {
        when(categoryRepository.findByNameIgnoreCase("Xxx")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getCategoryByName("Xxx"));
    }

    @Test
    void getActive_ok() {
        when(categoryRepository.findByActiveTrue()).thenReturn(List.of(category));
        when(categoryMapper.toDtoList(anyList())).thenReturn(List.of(categoryDto));

        List<CategoryResponseDto> result = categoryService.getActiveCategories();

        assertEquals(1, result.size());
    }

    @Test
    void update_ok() {
        CategoryUpdateRequestDto req = new CategoryUpdateRequestDto();
        req.setName("Hogar");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCase("Hogar")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryDto);

        CategoryResponseDto result = categoryService.updateCategory(1L, req);

        assertNotNull(result);
        verify(categoryRepository).save(category);
    }

    @Test
    void update_falla_categoriaNoExiste() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateCategory(1L, new CategoryUpdateRequestDto()));
    }

    @Test
    void update_falla_nombreDuplicado() {
        CategoryUpdateRequestDto req = new CategoryUpdateRequestDto();
        req.setName("Otra");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCase("Otra")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> categoryService.updateCategory(1L, req));
    }

    @Test
    void activate_ok() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryDto);

        CategoryResponseDto result = categoryService.activateCategory(1L);

        assertTrue(result.getActive());
    }

    @Test
    void activate_notFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.activateCategory(1L));
    }

    @Test
    void deactivate_ok() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        categoryService.deactivateCategory(1L);

        assertFalse(category.getActive());
    }

    @Test
    void deactivate_falla_conProductos() {
        category.getProducts().add(new Product()); // simula dependencia
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(ResourceHasDependenciesException.class,
                () -> categoryService.deactivateCategory(1L));
    }

    @Test
    void deactivate_notFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.deactivateCategory(1L));
    }

    @Test
    void delete_ok() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void delete_falla_conProductos() {
        category.getProducts().add(new Product());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(ResourceHasDependenciesException.class,
                () -> categoryService.deleteCategory(1L));
    }

    @Test
    void delete_notFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.deleteCategory(1L));
    }
}
