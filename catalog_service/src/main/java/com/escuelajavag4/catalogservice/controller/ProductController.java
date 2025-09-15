package com.escuelajavag4.catalogservice.controller;

import com.escuelajavag4.catalogservice.model.dto.request.ProductCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.request.ProductUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.ProductResponseDto;
import com.escuelajavag4.catalogservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponseDto createProduct(@Valid @RequestBody ProductCreateRequestDto createRequestDto) {
        return productService.createProduct(createRequestDto);
    }

    @GetMapping
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/search")
    public List<ProductResponseDto> searchProductsByName(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }

    @GetMapping("/active")
    public List<ProductResponseDto> getActiveProducts() {
        return productService.getActiveProducts();
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductResponseDto> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/category/{categoryId}/active")
    public List<ProductResponseDto> getActiveProductsByCategory(@PathVariable Long categoryId) {
        return productService.getActiveProductsByCategory(categoryId);
    }

    @GetMapping("/brand/{marcaId}")
    public List<ProductResponseDto> getProductsByMarca(@PathVariable Long marcaId) {
        return productService.getProductsByMarca(marcaId);
    }

    @GetMapping("/brand/{marcaId}/active")
    public List<ProductResponseDto> getActiveProductsByMarca(@PathVariable Long marcaId) {
        return productService.getActiveProductsByMarca(marcaId);
    }

    @GetMapping("/category/{categoryId}/brand/{marcaId}")
    public List<ProductResponseDto> getProductsByCategoryAndMarca(
            @PathVariable Long categoryId,
            @PathVariable Long marcaId) {
        return productService.getProductsByCategoryAndMarca(categoryId, marcaId);
    }

    @GetMapping("/category/{categoryId}/brand/{marcaId}/active")
    public List<ProductResponseDto> getActiveProductsByCategoryAndMarca(
            @PathVariable Long categoryId,
            @PathVariable Long marcaId) {
        return productService.getActiveProductsByCategoryAndMarca(categoryId, marcaId);
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequestDto updateRequestDto) {
        return productService.updateProduct(id, updateRequestDto);
    }

    @PatchMapping("/{id}/activate")
    public ProductResponseDto activateProduct(@PathVariable Long id) {
        return productService.activateProduct(id);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
