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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<CategoryResponseDto> response = categoryController.createCategory(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Electronics", response.getBody().getName());

        verify(categoryService).createCategory(request);
    }

    @Test
    void testGetAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(List.of(sampleCategory));

        ResponseEntity<List<CategoryResponseDto>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(categoryService).getAllCategories();
    }

    @Test
    void testGetCategoryById() {
        when(categoryService.getCategoryById(1L)).thenReturn(sampleCategory);

        ResponseEntity<CategoryResponseDto> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(categoryService).getCategoryById(1L);
    }

    @Test
    void testGetCategoryByIdWithProducts() {
        when(categoryService.getCategoryByIdWithProducts(1L)).thenReturn(sampleCategory);

        ResponseEntity<CategoryResponseDto> response = categoryController.getCategoryByIdWithProducts(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(categoryService).getCategoryByIdWithProducts(1L);
    }

    @Test
    void testGetCategoryByName() {
        when(categoryService.getCategoryByName("Electronics")).thenReturn(sampleCategory);

        ResponseEntity<CategoryResponseDto> response = categoryController.getCategoryByName("Electronics");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Electronics", response.getBody().getName());

        verify(categoryService).getCategoryByName("Electronics");
    }

    @Test
    void testGetActiveCategories() {
        when(categoryService.getActiveCategories()).thenReturn(List.of(sampleCategory));

        ResponseEntity<List<CategoryResponseDto>> response = categoryController.getActiveCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get(0).getActive());

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

        ResponseEntity<CategoryResponseDto> response = categoryController.updateCategory(1L, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Electronics", response.getBody().getName());

        verify(categoryService).updateCategory(1L, updateRequest);
    }

    @Test
    void testDeactivateCategory() {
        doNothing().when(categoryService).deactivateCategory(1L);

        ResponseEntity<Void> response = categoryController.deactivateCategory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(categoryService).deactivateCategory(1L);
    }

    @Test
    void testActivateCategory() {
        when(categoryService.activateCategory(1L)).thenReturn(sampleCategory);

        ResponseEntity<CategoryResponseDto> response = categoryController.activateCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getActive());

        verify(categoryService).activateCategory(1L);
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(categoryService).deleteCategory(1L);
    }
}
