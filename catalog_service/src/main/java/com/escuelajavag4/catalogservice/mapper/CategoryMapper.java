package com.escuelajavag4.catalogservice.mapper;

import com.escuelajavag4.catalogservice.model.dto.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.CategoryResponseDto;
import com.escuelajavag4.catalogservice.model.dto.CategoryUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProductMapper.class })
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


    @Mapping(target = "products", ignore = true)
    CategoryResponseDto toResponseDto(Category entity);

    CategoryResponseDto toResponseDtoWithProducts(Category entity);

    List<CategoryResponseDto> toDtoList(List<Category> entities);
}