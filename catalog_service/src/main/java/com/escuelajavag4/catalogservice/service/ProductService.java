package com.escuelajavag4.catalogservice.service;

import com.escuelajavag4.catalogservice.model.dto.request.ProductCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.ProductResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.ProductUpdateRequestDto;

import java.util.List;

public interface ProductService {

        ProductResponseDto createProduct(ProductCreateRequestDto createRequestDto);

        List<ProductResponseDto> getAllProducts();

        ProductResponseDto getProductById(Long id);

        ProductResponseDto getProductByCode(String code);

        List<ProductResponseDto> getActiveProducts();

        List<ProductResponseDto> getProductsByCategory(Long categoryId);

        List<ProductResponseDto> getActiveProductsByCategory(Long categoryId);

        List<ProductResponseDto> getProductsByMarca(Long marcaId);

        List<ProductResponseDto> getActiveProductsByMarca(Long marcaId);

        List<ProductResponseDto> searchProductsByName(String name);

        List<ProductResponseDto> getProductsByCategoryAndMarca(Long categoryId, Long marcaId);

        List<ProductResponseDto> getActiveProductsByCategoryAndMarca(Long categoryId, Long marcaId);

        ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto updateRequestDto);

        ProductResponseDto activateProduct(Long id);

        void deactivateProduct(Long id);

        void deleteProduct(Long id);
}
