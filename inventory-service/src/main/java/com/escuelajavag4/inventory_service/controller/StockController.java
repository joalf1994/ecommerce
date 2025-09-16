package com.escuelajavag4.inventory_service.controller;

import com.escuelajavag4.inventory_service.model.dto.request.StockCreateRequestDto;
import com.escuelajavag4.inventory_service.model.dto.request.StockReservedResponseDto;
import com.escuelajavag4.inventory_service.model.dto.response.StockResponseDto;
import com.escuelajavag4.inventory_service.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/inventory/reservations")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public List<StockResponseDto> getListStock() {
        return stockService.getListStock();
    }

    @GetMapping("/{productId}")
    public StockResponseDto getStockById(@PathVariable Long productId) {
        return stockService.getStockById(productId);
    }

    @PostMapping
    public StockResponseDto createStock(@RequestBody StockCreateRequestDto stockCreateRequestDto) {
        return  stockService.saveStock(stockCreateRequestDto);
    }

    @PatchMapping("/{productId}/reserve")
    public StockReservedResponseDto reserveStock(
            @PathVariable Long productId,
            @RequestParam int cantidad
    ) {
        return  stockService.reserveStock(productId, cantidad);
    }

}
