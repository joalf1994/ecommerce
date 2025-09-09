package com.escuelajavag4.catalogservice.controller;

import com.escuelajavag4.catalogservice.model.dto.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.CategoryResponseDto;
import com.escuelajavag4.catalogservice.model.dto.CategoryUpdateRequestDto;
import com.escuelajavag4.catalogservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryCreateRequestDto createRequestDto) {
        CategoryResponseDto createdCategory = categoryService.createCategory(createRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        CategoryResponseDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}/with-products")
    public ResponseEntity<CategoryResponseDto> getCategoryByIdWithProducts(@PathVariable Long id) {
        CategoryResponseDto category = categoryService.getCategoryByIdWithProducts(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/search")
    public ResponseEntity<CategoryResponseDto> getCategoryByName(@RequestParam String name) {
        CategoryResponseDto category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponseDto>> getActiveCategories() {
        List<CategoryResponseDto> activeCategories = categoryService.getActiveCategories();
        return ResponseEntity.ok(activeCategories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequestDto updateRequestDto) {

        CategoryResponseDto updatedCategory = categoryService.updateCategory(id, updateRequestDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCategory(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CategoryResponseDto> activateCategory(@PathVariable Long id) {
        CategoryResponseDto activatedCategory = categoryService.activateCategory(id);
        return ResponseEntity.ok(activatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
