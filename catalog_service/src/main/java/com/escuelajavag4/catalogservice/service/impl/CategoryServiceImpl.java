package com.escuelajavag4.catalogservice.service.impl;

import com.escuelajavag4.catalogservice.exception.DuplicateResourceException;
import com.escuelajavag4.catalogservice.exception.ResourceHasDependenciesException;
import com.escuelajavag4.catalogservice.exception.ResourceNotFoundException;
import com.escuelajavag4.catalogservice.mapper.CategoryMapper;
import com.escuelajavag4.catalogservice.model.dto.request.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.CategoryResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.CategoryUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Category;
import com.escuelajavag4.catalogservice.repository.CategoryRepository;
import com.escuelajavag4.catalogservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto createCategory(CategoryCreateRequestDto createRequestDto) {
        if (categoryRepository.existsByNameIgnoreCase(createRequestDto.getName())) {
            throw new DuplicateResourceException("Categoría", "nombre", createRequestDto.getName());
        }

        Category category = categoryMapper.toEntity(createRequestDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryByIdWithProducts(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));
        return categoryMapper.toResponseDtoWithProducts(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryByName(String name) {
        Category category = categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", name));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getActiveCategories() {
        List<Category> activeCategories = categoryRepository.findByActiveTrue();
        return categoryMapper.toDtoList(activeCategories);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryUpdateRequestDto updateRequestDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));

        if (updateRequestDto.getName() != null &&
                !updateRequestDto.getName().equalsIgnoreCase(existingCategory.getName()) &&
                categoryRepository.existsByNameIgnoreCase(updateRequestDto.getName())) {
            throw new DuplicateResourceException("Categoría", "nombre", updateRequestDto.getName());
        }

        categoryMapper.updateEntityFromDto(updateRequestDto, existingCategory);
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toResponseDto(updatedCategory);
    }

    @Override
    public CategoryResponseDto activateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));

        category.setActive(true);
        Category activatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDto(activatedCategory);
    }

    @Override
    public void deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));

        if (!category.getProducts().isEmpty()) {
            throw new ResourceHasDependenciesException("Categoría", "productos");
        }

        category.setActive(false);
        categoryRepository.save(category);
    }


    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));

        if (!category.getProducts().isEmpty()) {
            throw new ResourceHasDependenciesException("Categoría", "productos");
        }

        categoryRepository.deleteById(id);
    }
}

