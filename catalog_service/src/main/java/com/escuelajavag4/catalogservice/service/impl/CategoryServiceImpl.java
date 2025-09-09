package com.escuelajavag4.catalogservice.service.impl;

import com.escuelajavag4.catalogservice.exception.CategoryAlreadyExistsException;
import com.escuelajavag4.catalogservice.exception.CategoryHasProductsException;
import com.escuelajavag4.catalogservice.exception.CategoryNotFoundException;
import com.escuelajavag4.catalogservice.mapper.CategoryMapper;
import com.escuelajavag4.catalogservice.model.dto.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.CategoryResponseDto;
import com.escuelajavag4.catalogservice.model.dto.CategoryUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Category;
import com.escuelajavag4.catalogservice.repository.CategoryRepository;
import com.escuelajavag4.catalogservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto createCategory(CategoryCreateRequestDto createRequestDto) {

        if (categoryRepository.existsByNameIgnoreCase(createRequestDto.getName())) {
            throw new CategoryAlreadyExistsException("Ya existe una categoría con el nombre: " + createRequestDto.getName());
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
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con ID: " + id));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryByIdWithProducts(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con ID: " + id));
        return categoryMapper.toResponseDtoWithProducts(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryByName(String name) {
        Optional<Category> categoryOpt = categoryRepository.findByNameIgnoreCase(name);
        if (categoryOpt.isEmpty()) {
            throw new CategoryNotFoundException("Categoría no encontrada con nombre: " + name);
        }
        return categoryMapper.toResponseDto(categoryOpt.get());
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
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con ID: " + id));

        if (updateRequestDto.getName() != null &&
                !updateRequestDto.getName().equalsIgnoreCase(existingCategory.getName()) &&
                categoryRepository.existsByNameIgnoreCase(updateRequestDto.getName())) {
            throw new CategoryAlreadyExistsException("Ya existe una categoría con el nombre: " + updateRequestDto.getName());
        }

        categoryMapper.updateEntityFromDto(updateRequestDto, existingCategory);
        Category updatedCategory = categoryRepository.save(existingCategory);

        return categoryMapper.toResponseDto(updatedCategory);
    }

    @Override
    public void deactivateCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con ID: " + id));

        category.setActive(false);
        categoryRepository.save(category);
    }

    @Override
    public CategoryResponseDto activateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con ID: " + id));

        category.setActive(true);
        Category activatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDto(activatedCategory);

    }

    @Override
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con ID: " + id));

        if (!category.getProducts().isEmpty()) {
            throw new CategoryHasProductsException("No se puede eliminar la categoría porque tiene productos asociados");
        }

        categoryRepository.deleteById(id);
    }

}
