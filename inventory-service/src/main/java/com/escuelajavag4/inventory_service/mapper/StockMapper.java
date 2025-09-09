package com.escuelajavag4.inventory_service.mapper;


import com.escuelajavag4.inventory_service.dto.StockDto;
import com.escuelajavag4.inventory_service.dto.StockSaveRequestDto;
import com.escuelajavag4.inventory_service.model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMapper {
    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    StockDto toDto(Stock stock);
    List<StockDto> toDtoList(List<Stock> stocks);

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Stock toEntity(StockSaveRequestDto stockSaveRequestDto);
}
