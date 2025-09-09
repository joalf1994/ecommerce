package com.escuelajavag4.inventory_service.service;

import com.escuelajavag4.inventory_service.model.Stock;

import java.util.List;

public interface IStockService {
    List<Stock> getListStock();
    Stock saveStock(Stock stock);
}
