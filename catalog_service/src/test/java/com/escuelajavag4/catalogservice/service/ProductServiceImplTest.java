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
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductMapper productMapper;
    @Mock private CategoryRepository categoryRepository;
    @Mock private MarcaRepository marcaRepository;

    @InjectMocks private ProductServiceImpl productService;

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
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponseDto(product)).thenReturn(productDto);

        ProductResponseDto result = productService.createProduct(req);

        assertNotNull(result);
        assertEquals("P001", result.getCode());
        verify(productRepository).save(product);
    }

    @Test
    void crear_falla_codigoDuplicado() {
        ProductCreateRequestDto req = new ProductCreateRequestDto();
        req.setCode("P001");

        when(productRepository.existsByCodeIgnoreCase("P001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> productService.createProduct(req));

        verify(productRepository, never()).save(any());
    }

    @Test
    void crear_falla_categoriaNoExiste() {
        ProductCreateRequestDto req = new ProductCreateRequestDto();
        req.setCode("P001");
        req.setCategoryId(10L);

        when(productRepository.existsByCodeIgnoreCase("P001")).thenReturn(false);
        when(categoryRepository.existsById(10L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> productService.createProduct(req));

        verify(productRepository, never()).save(any());
    }

    @Test
    void crear_falla_marcaNoExiste() {
        ProductCreateRequestDto req = new ProductCreateRequestDto();
        req.setCode("P001");
        req.setCategoryId(10L);
        req.setMarcaId(20L);

        when(productRepository.existsByCodeIgnoreCase("P001")).thenReturn(false);
        when(categoryRepository.existsById(10L)).thenReturn(true);
        when(marcaRepository.existsById(20L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> productService.createProduct(req));

        verify(productRepository, never()).save(any());
    }

    @Test
    void crear_falla_productoNoSeEncuentraDespuesDeGuardar() {
        ProductCreateRequestDto req = new ProductCreateRequestDto();
        req.setCode("P001");
        req.setCategoryId(10L);
        req.setMarcaId(20L);

        when(productRepository.existsByCodeIgnoreCase("P001")).thenReturn(false);
        when(categoryRepository.existsById(10L)).thenReturn(true);
        when(marcaRepository.existsById(20L)).thenReturn(true);
        when(productMapper.toEntity(req)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.createProduct(req));
    }

    @Test
    void getAllProducts_ok() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toResponseDto(product)).thenReturn(productDto);

        List<ProductResponseDto> result = productService.getAllProducts();

        assertEquals(1, result.size());
    }

    @Test
    void getProductById_ok() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponseDto(product)).thenReturn(productDto);

        ProductResponseDto result = productService.getProductById(1L);

        assertEquals("P001", result.getCode());
    }

    @Test
    void getProductById_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(1L));
    }

    @Test
    void getProductByCode_ok() {
        when(productRepository.findByCodeIgnoreCase("P001")).thenReturn(Optional.of(product));
        when(productMapper.toResponseDto(product)).thenReturn(productDto);

        ProductResponseDto result = productService.getProductByCode("P001");

        assertEquals("P001", result.getCode());
    }

    @Test
    void getProductByCode_notFound() {
        when(productRepository.findByCodeIgnoreCase("X999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductByCode("X999"));
    }

    @Test
    void update_ok() {
        ProductUpdateRequestDto req = new ProductUpdateRequestDto();
        req.setCode("NEW01");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsByCodeIgnoreCase("NEW01")).thenReturn(false);

        Product updated = new Product();
        updated.setId(1L);
        updated.setCode("NEW01");

        when(productRepository.save(product)).thenReturn(updated);
        when(productMapper.toResponseDto(updated)).thenReturn(productDto);

        ProductResponseDto result = productService.updateProduct(1L, req);

        assertNotNull(result);
        verify(productRepository).save(product);
    }

    @Test
    void update_falla_productoNoExiste() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(99L, new ProductUpdateRequestDto()));
    }

    @Test
    void update_falla_codigoDuplicado() {
        ProductUpdateRequestDto req = new ProductUpdateRequestDto();
        req.setCode("NEW01");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsByCodeIgnoreCase("NEW01")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> productService.updateProduct(1L, req));
    }

    @Test
    void update_falla_categoriaNoExiste() {
        ProductUpdateRequestDto req = new ProductUpdateRequestDto();
        req.setCategoryId(50L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.existsById(50L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(1L, req));
    }

    @Test
    void update_falla_marcaNoExiste() {
        ProductUpdateRequestDto req = new ProductUpdateRequestDto();
        req.setMarcaId(77L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(marcaRepository.existsById(77L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(1L, req));
    }


    @Test
    void activate_ok() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponseDto(product)).thenReturn(productDto);

        ProductResponseDto result = productService.activateProduct(1L);

        assertTrue(result.getActive());
    }

    @Test
    void activate_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.activateProduct(1L));
    }

    @Test
    void deactivate_ok() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        productService.deactivateProduct(1L);

        assertFalse(product.getActive());
    }

    @Test
    void deactivate_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.deactivateProduct(1L));
    }

    @Test
    void delete_ok() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void delete_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(1L));
    }
}



