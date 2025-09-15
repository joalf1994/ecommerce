package com.escuelajavag4.catalogservice.mapper;

import com.escuelajavag4.catalogservice.model.dto.request.ProductCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.ProductResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.ProductUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Category;
import com.escuelajavag4.catalogservice.model.entity.Marca;
import com.escuelajavag4.catalogservice.model.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring",uses = {CategoryMapper.class,MarcaMapper.class})
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "fromId")
    @Mapping(target = "marca", source = "marcaId", qualifiedByName = "fromId")
    Product toEntity(ProductCreateRequestDto dto);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "marcaId", source = "marca.id")
    ProductResponseDto toResponseDto(Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "fromId")
    @Mapping(target = "marca", source = "marcaId", qualifiedByName = "fromId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProductUpdateRequestDto dto, @MappingTarget Product entity);
}
