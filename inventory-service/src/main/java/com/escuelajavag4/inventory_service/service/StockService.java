package com.escuelajavag4.inventory_service.service;

import com.escuelajavag4.inventory_service.model.dto.request.StockCreateRequestDto;
import com.escuelajavag4.inventory_service.model.dto.request.StockReservedResponseDto;
import com.escuelajavag4.inventory_service.model.dto.response.StockResponseDto;

import java.util.List;

public interface StockService {
    List<StockResponseDto> getListStock();
    List<StockResponseDto> getStockById(Long productId);
    StockResponseDto saveStock(StockCreateRequestDto stockCreateRequestDto);
    StockReservedResponseDto reserveStock(Long productId, int cantidad);
    boolean validateStock(Long productId, int cantidad);
}
