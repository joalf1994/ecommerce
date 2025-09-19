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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

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

        ResponseEntity<ProductResponseDto> response = productController.createProduct(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Laptop Gamer", response.getBody().getName());

        verify(productService).createProduct(request);
    }

    @Test
    void testGetAllProducts() {
        when(productService.getAllProducts()).thenReturn(List.of(sampleProduct));

        ResponseEntity<List<ProductResponseDto>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(productService).getAllProducts();
    }

    @Test
    void testGetProductById() {
        when(productService.getProductById(1L)).thenReturn(sampleProduct);

        ResponseEntity<ProductResponseDto> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(productService).getProductById(1L);
    }

    @Test
    void testSearchProductsByName() {
        when(productService.searchProductsByName("Laptop")).thenReturn(List.of(sampleProduct));

        ResponseEntity<List<ProductResponseDto>> response = productController.searchProductsByName("Laptop");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(productService).searchProductsByName("Laptop");
    }

    @Test
    void testGetActiveProducts() {
        when(productService.getActiveProducts()).thenReturn(List.of(sampleProduct));

        ResponseEntity<List<ProductResponseDto>> response = productController.getActiveProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get(0).getActive());

        verify(productService).getActiveProducts();
    }

    @Test
    void testGetProductsByCategory() {
        when(productService.getProductsByCategory(1L)).thenReturn(List.of(sampleProduct));

        ResponseEntity<List<ProductResponseDto>> response = productController.getProductsByCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(productService).getProductsByCategory(1L);
    }


    @Test
    void testGetProductsByMarca() {
        when(productService.getProductsByMarca(1L)).thenReturn(List.of(sampleProduct));

        ResponseEntity<List<ProductResponseDto>> response = productController.getProductsByMarca(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(productService).getProductsByMarca(1L);
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

        ResponseEntity<ProductResponseDto> response = productController.updateProduct(1L, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Laptop Ultra", response.getBody().getName());

        verify(productService).updateProduct(1L, updateRequest);
    }

    @Test
    void testActivateProduct() {
        when(productService.activateProduct(1L)).thenReturn(sampleProduct);

        ResponseEntity<ProductResponseDto> response = productController.activateProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getActive());

        verify(productService).activateProduct(1L);
    }

    @Test
    void testDeactivateProduct() {
        doNothing().when(productService).deactivateProduct(1L);

        ResponseEntity<Void> response = productController.deactivateProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(productService).deactivateProduct(1L);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(productService).deleteProduct(1L);
    }
}
