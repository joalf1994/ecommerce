package com.escuelajavag4.catalogservice.mapper;

import com.escuelajavag4.catalogservice.model.dto.request.CategoryCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.CategoryResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.CategoryUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface CategoryMapper {
//hola

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
    @Named("simple")
    CategoryResponseDto toResponseDto(Category entity);

    @Named("withProducts")
    CategoryResponseDto toResponseDtoWithProducts(Category entity);

    @IterableMapping(qualifiedByName = "simple")
    List<CategoryResponseDto> toDtoList(List<Category> entities);

    @IterableMapping(qualifiedByName = "withProducts")
    List<CategoryResponseDto> toDtoListWithProducts(List<Category> entities);
}