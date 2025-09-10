package com.escuelajavag4.catalogservice.service;

import com.escuelajavag4.catalogservice.model.dto.request.ProductCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.ProductResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.ProductUpdateRequestDto;

import java.util.List;

public interface ProductService {

    // Crear un producto
    ProductResponseDto createProduct(ProductCreateRequestDto createRequestDto);

    // Obtener todos los productos
    List<ProductResponseDto> getAllProducts();

    // Obtener un producto por ID
    ProductResponseDto getProductById(Long id);

    // Obtener un producto por código
    ProductResponseDto getProductByCode(String code);

    // Obtener productos activos
    List<ProductResponseDto> getActiveProducts();

    // Obtener productos por categoría
    List<ProductResponseDto> getProductsByCategory(Long categoryId);

    // Obtener productos activos por categoría
    List<ProductResponseDto> getActiveProductsByCategory(Long categoryId);

    // Obtener productos por marca
    List<ProductResponseDto> getProductsByMarca(Long marcaId);

    // Obtener productos activos por marca
    List<ProductResponseDto> getActiveProductsByMarca(Long marcaId);

    // Obtener productos por nombre (búsqueda parcial)
    List<ProductResponseDto> searchProductsByName(String name);

    // Obtener productos por categoría y marca
    List<ProductResponseDto> getProductsByCategoryAndMarca(Long categoryId, Long marcaId);

    // Obtener productos activos por categoría y marca
    List<ProductResponseDto> getActiveProductsByCategoryAndMarca(Long categoryId, Long marcaId);

    // Actualizar producto
    ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto updateRequestDto);

    // Activar producto
    ProductResponseDto activateProduct(Long id);

    // Desactivar producto
    void deactivateProduct(Long id);

    // Eliminar producto
    void deleteProduct(Long id);
}
