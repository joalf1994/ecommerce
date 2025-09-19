package com.escuelajavag4.order_service.feign;

import com.escuelajavag4.order_service.dto.product.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalog-service")
public interface CatalogClient {
    @GetMapping("/api/products/{id}")
    ProductDto getProductById(@PathVariable Long id);
}
