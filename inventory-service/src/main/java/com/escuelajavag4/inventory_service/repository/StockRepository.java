package com.escuelajavag4.inventory_service.repository;

import com.escuelajavag4.inventory_service.model.dto.response.StockResponseDto;
import com.escuelajavag4.inventory_service.model.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    boolean existsByProductIdAndWarehouseId(Long productId, Long warehouseId);
    List<Stock> findAllByProductId(Long productId);
//    Stock findByProductId(Long productId);
}
