package com.escuelajavag4.order_service.feign;

import com.escuelajavag4.order_service.dto.inventory.StockDto;
import com.escuelajavag4.order_service.dto.inventory.StockReservedDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/api/inventory/{productId}")
    StockDto getStockByProductId(@PathVariable Long productId);

    @PatchMapping("/api/inventory/{productId}/reserve")
    StockReservedDto reserveStock(@PathVariable Long productId, @RequestParam int cantidad);

}