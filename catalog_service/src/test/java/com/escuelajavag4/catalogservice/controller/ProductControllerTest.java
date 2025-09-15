package com.escuelajavag4.catalogservice.controller;

import com.escuelajavag4.catalogservice.model.dto.request.ProductCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.request.ProductUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.ProductResponseDto;
import com.escuelajavag4.catalogservice.service.ProductService;
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
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductResponseDto sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new ProductResponseDto();
        sampleProduct.setId(1L);
        sampleProduct.setName("Laptop Gamer");
        sampleProduct.setActive(true);
    }

    @Test
    void testCreateProduct() {
        ProductCreateRequestDto request = new ProductCreateRequestDto();
        request.setName("Laptop Gamer");

        when(productService.createProduct(request)).thenReturn(sampleProduct);

        ProductResponseDto result = productController.createProduct(request);
        assertNotNull(result);
        assertEquals("Laptop Gamer", result.getName());

        verify(productService).createProduct(request);
    }

    @Test
    void testGetAllProducts() {
        when(productService.getAllProducts()).thenReturn(List.of(sampleProduct));

        List<ProductResponseDto> result = productController.getAllProducts();
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(productService).getAllProducts();
    }

    @Test
    void testGetProductById() {
        when(productService.getProductById(1L)).thenReturn(sampleProduct);

        ProductResponseDto result = productController.getProductById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(productService).getProductById(1L);
    }

    @Test
    void testSearchProductsByName() {
        when(productService.searchProductsByName("Laptop")).thenReturn(List.of(sampleProduct));

        List<ProductResponseDto> result = productController.searchProductsByName("Laptop");
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(productService).searchProductsByName("Laptop");
    }

    @Test
    void testGetActiveProducts() {
        when(productService.getActiveProducts()).thenReturn(List.of(sampleProduct));

        List<ProductResponseDto> result = productController.getActiveProducts();
        assertNotNull(result);
        assertTrue(result.get(0).getActive());

        verify(productService).getActiveProducts();
    }

    @Test
    void testGetProductsByCategory() {
        when(productService.getProductsByCategory(1L)).thenReturn(List.of(sampleProduct));

        List<ProductResponseDto> result = productController.getProductsByCategory(1L);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(productService).getProductsByCategory(1L);
    }

    @Test
    void testGetActiveProductsByCategory() {
        when(productService.getActiveProductsByCategory(1L)).thenReturn(List.of(sampleProduct));

        List<ProductResponseDto> result = productController.getActiveProductsByCategory(1L);
        assertNotNull(result);
        assertTrue(result.get(0).getActive());

        verify(productService).getActiveProductsByCategory(1L);
    }

    @Test
    void testGetProductsByMarca() {
        when(productService.getProductsByMarca(1L)).thenReturn(List.of(sampleProduct));

        List<ProductResponseDto> result = productController.getProductsByMarca(1L);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(productService).getProductsByMarca(1L);
    }

    @Test
    void testGetActiveProductsByMarca() {
        when(productService.getActiveProductsByMarca(1L)).thenReturn(List.of(sampleProduct));

        List<ProductResponseDto> result = productController.getActiveProductsByMarca(1L);
        assertNotNull(result);
        assertTrue(result.get(0).getActive());

        verify(productService).getActiveProductsByMarca(1L);
    }

    @Test
    void testGetProductsByCategoryAndMarca() {
        when(productService.getProductsByCategoryAndMarca(1L, 1L)).thenReturn(List.of(sampleProduct));

        List<ProductResponseDto> result = productController.getProductsByCategoryAndMarca(1L, 1L);
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(productService).getProductsByCategoryAndMarca(1L, 1L);
    }

    @Test
    void testGetActiveProductsByCategoryAndMarca() {
        when(productService.getActiveProductsByCategoryAndMarca(1L, 1L)).thenReturn(List.of(sampleProduct));

        List<ProductResponseDto> result = productController.getActiveProductsByCategoryAndMarca(1L, 1L);
        assertNotNull(result);
        assertTrue(result.get(0).getActive());

        verify(productService).getActiveProductsByCategoryAndMarca(1L, 1L);
    }

    @Test
    void testUpdateProduct() {
        ProductUpdateRequestDto updateRequest = new ProductUpdateRequestDto();
        updateRequest.setName("Laptop Ultra");

        ProductResponseDto updatedProduct = new ProductResponseDto();
        updatedProduct.setId(1L);
        updatedProduct.setName("Laptop Ultra");
        updatedProduct.setActive(true);


        when(productService.updateProduct(1L, updateRequest)).thenReturn(updatedProduct);

        ProductResponseDto result = productController.updateProduct(1L, updateRequest);
        assertNotNull(result);
        assertEquals("Laptop Ultra", result.getName());

        verify(productService).updateProduct(1L, updateRequest);
    }

    @Test
    void testActivateProduct() {
        when(productService.activateProduct(1L)).thenReturn(sampleProduct);

        ProductResponseDto result = productController.activateProduct(1L);
        assertNotNull(result);
        assertTrue(result.getActive());

        verify(productService).activateProduct(1L);
    }

    @Test
    void testDeactivateProduct() {
        doNothing().when(productService).deactivateProduct(1L);

        productController.deactivateProduct(1L);

        verify(productService).deactivateProduct(1L);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        productController.deleteProduct(1L);

        verify(productService).deleteProduct(1L);
    }
}
