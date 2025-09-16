package com.escuelajavag4.order_service.feign;

import com.escuelajavag4.order_service.dto.inventory.StockDto;
import com.escuelajavag4.order_service.dto.inventory.StockReservedDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/inventory/reservations/{productId}")
    StockDto getStockByProductId(@PathVariable Long productId);

    @PutMapping("/inventory/reservations/{productId}/reserve")
    StockReservedDto reserveStock(@PathVariable Long productId, @RequestParam int cantidad);

}