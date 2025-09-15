package com.escuelajavag4.catalogservice.controller;

import com.escuelajavag4.catalogservice.model.dto.request.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.request.CategoryUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.CategoryResponseDto;
import com.escuelajavag4.catalogservice.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryResponseDto sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new CategoryResponseDto();
        sampleCategory.setId(1L);
        sampleCategory.setName("Electronics");
        sampleCategory.setActive(true);
    }

    @Test
    void testCreateCategory() {
        CategoryCreateRequestDto request = new CategoryCreateRequestDto();
        request.setName("Electronics");

        when(categoryService.createCategory(request)).thenReturn(sampleCategory);

        CategoryResponseDto result = categoryController.createCategory(request);
        assertNotNull(result);
        assertEquals("Electronics", result.getName());

        verify(categoryService).createCategory(request);
    }

    @Test
    void testGetAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(List.of(sampleCategory));

        List<CategoryResponseDto> result = categoryController.getAllCategories();
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(categoryService).getAllCategories();
    }

    @Test
    void testGetCategoryById() {
        when(categoryService.getCategoryById(1L)).thenReturn(sampleCategory);

        CategoryResponseDto result = categoryController.getCategoryById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(categoryService).getCategoryById(1L);
    }

    @Test
    void testGetCategoryByIdWithProducts() {
        when(categoryService.getCategoryByIdWithProducts(1L)).thenReturn(sampleCategory);

        CategoryResponseDto result = categoryController.getCategoryByIdWithProducts(1L);
        assertNotNull(result);

        verify(categoryService).getCategoryByIdWithProducts(1L);
    }

    @Test
    void testGetCategoryByName() {
        when(categoryService.getCategoryByName("Electronics")).thenReturn(sampleCategory);

        CategoryResponseDto result = categoryController.getCategoryByName("Electronics");
        assertNotNull(result);
        assertEquals("Electronics", result.getName());

        verify(categoryService).getCategoryByName("Electronics");
    }

    @Test
    void testGetActiveCategories() {
        when(categoryService.getActiveCategories()).thenReturn(List.of(sampleCategory));

        List<CategoryResponseDto> result = categoryController.getActiveCategories();
        assertNotNull(result);
        assertTrue(result.get(0).getActive());

        verify(categoryService).getActiveCategories();
    }

    @Test
    void testUpdateCategory() {
        CategoryUpdateRequestDto updateRequest = new CategoryUpdateRequestDto();
        updateRequest.setName("Updated Electronics");

        CategoryResponseDto updatedCategory = new CategoryResponseDto();
        updatedCategory.setId(1L);
        updatedCategory.setName("Updated Electronics");
        updatedCategory.setActive(true);

        when(categoryService.updateCategory(1L, updateRequest)).thenReturn(updatedCategory);

        CategoryResponseDto result = categoryController.updateCategory(1L, updateRequest);
        assertNotNull(result);
        assertEquals("Updated Electronics", result.getName());

        verify(categoryService).updateCategory(1L, updateRequest);
    }

    @Test
    void testDeactivateCategory() {
        doNothing().when(categoryService).deactivateCategory(1L);

        categoryController.deactivateCategory(1L);

        verify(categoryService).deactivateCategory(1L);
    }

    @Test
    void testActivateCategory() {
        when(categoryService.activateCategory(1L)).thenReturn(sampleCategory);

        CategoryResponseDto result = categoryController.activateCategory(1L);
        assertNotNull(result);
        assertTrue(result.getActive());

        verify(categoryService).activateCategory(1L);
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryService).deleteCategory(1L);

        categoryController.deleteCategory(1L);

        verify(categoryService).deleteCategory(1L);
    }
}