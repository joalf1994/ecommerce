package com.escuelajavag4.catalogservice.mapper;

import com.escuelajavag4.catalogservice.model.dto.request.MarcaCreateRequestDto;
import com.escuelajavag4.catalogservice.model.dto.response.MarcaResponseDto;
import com.escuelajavag4.catalogservice.model.dto.request.MarcaUpdateRequestDto;
import com.escuelajavag4.catalogservice.model.entity.Marca;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MarcaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Marca toEntity(MarcaCreateRequestDto dto);

    @Named("simpleMarca")
    @Mapping(target = "products", ignore = true)
    MarcaResponseDto toResponseDto(Marca entity);

    @Named("withProducts")
    MarcaResponseDto toResponseDtoWithProducts(Marca entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(MarcaUpdateRequestDto dto, @MappingTarget Marca entity);

    @Named("fromId")
    default Marca fromId(Long id) {
        if (id == null) return null;
        Marca marca = new Marca();
        marca.setId(id);
        return marca;
    }
}
