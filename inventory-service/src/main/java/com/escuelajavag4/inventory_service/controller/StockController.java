package com.escuelajavag4.inventory_service.controller;

import com.escuelajavag4.inventory_service.model.dto.request.StockCreateRequestDto;
import com.escuelajavag4.inventory_service.model.dto.request.StockReservedResponseDto;
import com.escuelajavag4.inventory_service.model.dto.response.StockResponseDto;
import com.escuelajavag4.inventory_service.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/inventory/reservations")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockResponseDto>> getListStock() {
        return ResponseEntity.ok(stockService.getListStock());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<StockResponseDto>> getStockById(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getStockById(productId));
    }

    @PostMapping
    public ResponseEntity<StockResponseDto> createStock(@RequestBody StockCreateRequestDto stockCreateRequestDto) {
        return  ResponseEntity.ok(stockService.saveStock(stockCreateRequestDto));
    }

    @PutMapping("/{productId}/reserve")
    public ResponseEntity<StockReservedResponseDto> reserveStock(
            @PathVariable Long productId,
            @RequestParam int cantidad
    ) {
        return  ResponseEntity.ok(stockService.reserveStock(productId, cantidad));
    }

    @GetMapping("/{productId}/validate")
    public ResponseEntity<Boolean> validateStock( @PathVariable Long productId,
                                  @RequestParam int cantidad){
        return ResponseEntity.ok(stockService.validateStock(productId, cantidad));
    }

}
