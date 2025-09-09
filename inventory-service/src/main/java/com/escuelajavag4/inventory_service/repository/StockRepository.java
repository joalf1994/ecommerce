package com.escuelajavag4.inventory_service.repository;

import com.escuelajavag4.inventory_service.model.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, Long> {
}
