package com.escuelajavag4.catalogservice.service;

import com.escuelajavag4.catalogservice.exception.DuplicateResourceException;
import com.escuelajavag4.catalogservice.exception.ResourceNotFoundException;
import com.escuelajavag4.catalogservice.mapper.ProductMapper;
import com.escuelajavag4.catalogservice.model.dto.request.ProductCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.request.ProductUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.ProductResponseDto;
import com.escuelajavag4.catalogservice.model.entity.Product;
import com.escuelajavag4.catalogservice.repository.CategoryRepository;
import com.escuelajavag4.catalogservice.repository.MarcaRepository;
import com.escuelajavag4.catalogservice.repository.ProductRepository;
import com.escuelajavag4.catalogservice.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductResponseDto productDto;

    @BeforeEach
    void init() {
        product = new Product();
        product.setId(1L);
        product.setCode("P001");
        product.setActive(true);

        productDto = new ProductResponseDto();
        productDto.setId(1L);
        productDto.setCode("P001");
        productDto.setActive(true);
    }

    @Test
    void crear_ok() {
        ProductCreateRequestDto req = new ProductCreateRequestDto();
        req.setCode("P001");
        req.setCategoryId(10L);
        req.setMarcaId(20L);

        when(productRepository.existsByCodeIgnoreCase("P001")).thenReturn(false);
        when(categoryRepository.existsById(10L)).thenReturn(true);
        when(marcaRepository.existsById(20L)).thenReturn(true);
        when(productMapper.toEntity(req)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponseDto(product)).thenReturn(productDto);

        ProductResponseDto result = productService.createProduct(req);

        assertNotNull(result);
        assertEquals("P001", result.getCode());
        verify(productRepository).save(product);
    }

    @Test
    void crear_codigoDuplicado() {
        ProductCreateRequestDto req = new ProductCreateRequestDto();
        req.setCode("P001");

        when(productRepository.existsByCodeIgnoreCase("P001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> productService.createProduct(req));
    }

    @Test
    void buscarPorId_ok() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponseDto(product)).thenReturn(productDto);

        ProductResponseDto result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void buscarPorId_noExiste() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(99L));
    }

    @Test
    void actualizar_ok() {
        ProductUpdateRequestDto req = new ProductUpdateRequestDto();
        req.setCode("P002");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsByCodeIgnoreCase("P002")).thenReturn(false);
        doNothing().when(productMapper).updateEntityFromDto(req, product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponseDto(product)).thenReturn(productDto);

        ProductResponseDto result = productService.updateProduct(1L, req);

        assertNotNull(result);
        verify(productRepository).save(product);
    }

    @Test
    void eliminar_ok() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }
}


