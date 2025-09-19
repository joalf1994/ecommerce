package com.escuelajavag4.inventory_service.feign;

import com.escuelajavag4.inventory_service.model.dto.response.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "catalog-service")
public interface CatalogClient {
    @GetMapping("/api/products/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id);
}
