package com.escuelajavag4.inventory_service.service.impl;

import com.escuelajavag4.inventory_service.model.Stock;
import com.escuelajavag4.inventory_service.repository.StockRepository;
import com.escuelajavag4.inventory_service.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StockServiceImpl implements IStockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public List<Stock> getListStock() {
        try {
            return (List<Stock>) stockRepository.findAll();
        }catch (Exception exception){
            throw new RuntimeException("Error al obtener stock");
        }
    }

    @Override
    public Stock saveStock(Stock stock) {
        try {
        return stockRepository.save(stock);
        }catch (Exception exception){
            throw new RuntimeException("Error al guarda Stock");
        }
    }
}
