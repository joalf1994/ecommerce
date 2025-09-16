package com.escuelajavag4.catalogservice.service;

import com.escuelajavag4.catalogservice.model.dto.request.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.CategoryResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.CategoryUpdateRequestDto;

import java.util.List;

public interface CategoryService {


    CategoryResponseDto createCategory(CategoryCreateRequestDto createRequestDto);

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto getCategoryById(Long id);

    CategoryResponseDto getCategoryByIdWithProducts(Long id);

    CategoryResponseDto getCategoryByName(String name);

    List<CategoryResponseDto> getActiveCategories();

    CategoryResponseDto updateCategory(Long id, CategoryUpdateRequestDto updateRequestDto);

    void deactivateCategory(Long id);

    CategoryResponseDto activateCategory(Long id);

    void deleteCategory(Long id);

}
