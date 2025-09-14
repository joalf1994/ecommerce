package com.escuelajavag4.catalogservice.mapper;

import com.escuelajavag4.catalogservice.model.dto.request.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.CategoryResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.CategoryUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryCreateRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(CategoryUpdateRequestDto dto, @MappingTarget Category entity);

    @Named("simpleCategory")
    @Mapping(target = "products", ignore = true)
    CategoryResponseDto toResponseDto(Category entity);

    @Named("withProducts")
    CategoryResponseDto toResponseDtoWithProducts(Category entity);

    @IterableMapping(qualifiedByName = "simpleCategory")
    List<CategoryResponseDto> toDtoList(List<Category> entities);

    @IterableMapping(qualifiedByName = "withProducts")
    List<CategoryResponseDto> toDtoListWithProducts(List<Category> entities);

    @Named("fromId")
    default Category fromId(Long id) {
        if (id == null) return null;
        Category category = new Category();
        category.setId(id);
        return category;
    }
}