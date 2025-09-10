package com.escuelajavag4.catalogservice.controller;

import com.escuelajavag4.catalogservice.model.dto.request.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.CategoryResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.CategoryUpdateRequestDto;
import com.escuelajavag4.catalogservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryCreateRequestDto createRequestDto) {
        return categoryService.createCategory(createRequestDto);
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/{id}/with-products")
    public CategoryResponseDto getCategoryByIdWithProducts(@PathVariable Long id) {
        return categoryService.getCategoryByIdWithProducts(id);
    }

    @GetMapping("/search")
    public CategoryResponseDto getCategoryByName(@RequestParam String name) {
        return categoryService.getCategoryByName(name);
    }

    @GetMapping("/active")
    public List<CategoryResponseDto> getActiveCategories() {
        return categoryService.getActiveCategories();
    }

    @PutMapping("/{id}")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequestDto updateRequestDto) {
        return categoryService.updateCategory(id, updateRequestDto);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivateCategory(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
    }

    @PatchMapping("/{id}/activate")
    public CategoryResponseDto activateCategory(@PathVariable Long id) {
        return categoryService.activateCategory(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}

