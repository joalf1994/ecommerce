package com.escuelajavag4.catalogservice.service.impl;

import com.escuelajavag4.catalogservice.exception.DuplicateResourceException;
import com.escuelajavag4.catalogservice.exception.ResourceNotFoundException;
import com.escuelajavag4.catalogservice.mapper.ProductMapper;
import com.escuelajavag4.catalogservice.model.dto.request.ProductCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.ProductResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.ProductUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Product;
import com.escuelajavag4.catalogservice.repository.CategoryRepository;
import com.escuelajavag4.catalogservice.repository.MarcaRepository;
import com.escuelajavag4.catalogservice.repository.ProductRepository;
import com.escuelajavag4.catalogservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final MarcaRepository marcaRepository;

    @Override
    public ProductResponseDto createProduct(ProductCreateRequestDto createRequestDto) {
        if (productRepository.existsByCodeIgnoreCase(createRequestDto.getCode())) {
            throw new DuplicateResourceException("Producto", "código", createRequestDto.getCode());
        }

        if (!categoryRepository.existsById(createRequestDto.getCategoryId())) {
            throw new ResourceNotFoundException("Categoría", createRequestDto.getCategoryId());
        }

        if (!marcaRepository.existsById(createRequestDto.getMarcaId())) {
            throw new ResourceNotFoundException("Marca", createRequestDto.getMarcaId());
        }

        Product product = productMapper.toEntity(createRequestDto);
        Product savedProduct = productRepository.save(product);
        Product reloaded = productRepository.findById(savedProduct.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", savedProduct.getId()));

        return productMapper.toResponseDto(reloaded);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        return productMapper.toResponseDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductByCode(String code) {
        Product product = productRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", code));
        return productMapper.toResponseDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getActiveProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByMarca(Long marcaId) {
        return productRepository.findByMarcaId(marcaId)
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto updateRequestDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));

        if (updateRequestDto.getCode() != null &&
                !updateRequestDto.getCode().equalsIgnoreCase(existingProduct.getCode()) &&
                productRepository.existsByCodeIgnoreCase(updateRequestDto.getCode())) {
            throw new DuplicateResourceException("Producto", "código", updateRequestDto.getCode());
        }

        if (updateRequestDto.getCategoryId() != null &&
                !categoryRepository.existsById(updateRequestDto.getCategoryId())) {
            throw new ResourceNotFoundException("Categoría", updateRequestDto.getCategoryId());
        }

        if (updateRequestDto.getMarcaId() != null &&
                !marcaRepository.existsById(updateRequestDto.getMarcaId())) {
            throw new ResourceNotFoundException("Marca", updateRequestDto.getMarcaId());
        }

        productMapper.updateEntityFromDto(updateRequestDto, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toResponseDto(updatedProduct);
    }

    @Override
    public ProductResponseDto activateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        product.setActive(true);
        Product activatedProduct = productRepository.save(product);
        return productMapper.toResponseDto(activatedProduct);
    }

    @Override
    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));

        productRepository.deleteById(id);
    }
}
