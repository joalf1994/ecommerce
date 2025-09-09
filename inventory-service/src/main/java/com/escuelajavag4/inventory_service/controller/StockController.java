package com.escuelajavag4.inventory_service.controller;

import com.escuelajavag4.inventory_service.dto.StockDto;
import com.escuelajavag4.inventory_service.dto.StockSaveRequestDto;
import com.escuelajavag4.inventory_service.mapper.StockMapper;
import com.escuelajavag4.inventory_service.model.Stock;
import com.escuelajavag4.inventory_service.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.escuelajavag4.inventory_service.mapper.StockMapper.INSTANCE;

import java.util.List;

@RestController()
@RequestMapping("/inventory/reservations")
public class StockController {

    @Autowired
    private IStockService stockService;
    private StockMapper stockMapper;

    @GetMapping
    public ResponseEntity<List<StockDto>> getListStock() {
        List<Stock> stocks = stockService.getListStock();
        List<StockDto> stockDtos = INSTANCE.toDtoList(stocks); // 👈 AQUÍ FALLA
        return ResponseEntity.ok(stockDtos);
    }

    @PostMapping
    public ResponseEntity<StockDto> createStock(@RequestBody StockSaveRequestDto stockSaveRequestDto) {
        Stock stock = INSTANCE.toEntity(stockSaveRequestDto);
        Stock savedStock = stockService.saveStock(stock);
        StockDto stockDto = INSTANCE.toDto(savedStock);
        return ResponseEntity.ok(stockDto);
    }
}
