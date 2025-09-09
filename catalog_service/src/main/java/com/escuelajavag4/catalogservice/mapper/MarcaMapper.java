package com.escuelajavag4.catalogservice.mapper;

import com.escuelajavag4.catalogservice.model.dto.MarcaCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.MarcaResponseDto;
import com.escuelajavag4.catalogservice.model.dto.MarcaUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Marca;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface MarcaMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", defaultValue = "true")
    Marca toEntity(MarcaCreateRequestDto dto);

    // Sin productos para evitar referencia circular
    @Mapping(target = "products", ignore = true)
    MarcaResponseDto toResponseDto(Marca entity);

    // Con productos cuando sea necesario
    MarcaResponseDto toResponseDtoWithProducts(Marca entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(MarcaUpdateRequestDto dto, @MappingTarget Marca entity);
}
