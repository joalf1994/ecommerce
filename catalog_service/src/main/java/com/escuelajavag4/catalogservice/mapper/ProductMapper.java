package com.escuelajavag4.catalogservice.mapper;

import com.escuelajavag4.catalogservice.model.dto.request.ProductCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.ProductResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.ProductUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Category;
import com.escuelajavag4.catalogservice.model.entity.Marca;
import com.escuelajavag4.catalogservice.model.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", defaultValue = "true")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategory")
    @Mapping(target = "marca", source = "marcaId", qualifiedByName = "marcaIdToMarca")
    Product toEntity(ProductCreateRequestDto dto);

    // Product response sin las colecciones de category y marca para evitar recursión infinita
    @Mapping(target = "category.products", ignore = true)
    @Mapping(target = "marca.products", ignore = true)
    ProductResponseDto toResponseDto(Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryIdToCategory")
    @Mapping(target = "marca", source = "marcaId", qualifiedByName = "marcaIdToMarca")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProductUpdateRequestDto dto, @MappingTarget Product entity);

    // Métodos auxiliares para mapear IDs a entidades
    @Named("categoryIdToCategory")
    default Category categoryIdToCategory(Long categoryId) {
        if (categoryId == null) return null;
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @Named("marcaIdToMarca")
    default Marca marcaIdToMarca(Long marcaId) {
        if (marcaId == null) return null;
        Marca marca = new Marca();
        marca.setId(marcaId);
        return marca;
    }
}
