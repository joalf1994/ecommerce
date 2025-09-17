package com.escuelajavag4.order_service.feign;

import com.escuelajavag4.order_service.dto.inventory.StockDto;
import com.escuelajavag4.order_service.dto.inventory.StockReservedDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/inventory/reservations/{productId}")
    Boolean hasSufficientStock(@PathVariable("productId") Long productId, @RequestParam("cantidad") int cantidad);

    @PutMapping("/inventory/reservations/{productId}/reserve")
    StockReservedDto reserveStock(@PathVariable Long productId, @RequestParam int cantidad);

}