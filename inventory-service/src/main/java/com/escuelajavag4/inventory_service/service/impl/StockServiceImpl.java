package com.escuelajavag4.inventory_service.service.impl;

import com.escuelajavag4.inventory_service.exception.DuplicateResourceException;
import com.escuelajavag4.inventory_service.mapper.StockMapper;
import com.escuelajavag4.inventory_service.model.dto.request.StockCreateRequestDto;
import com.escuelajavag4.inventory_service.model.dto.request.StockReservedResponseDto;
import com.escuelajavag4.inventory_service.model.dto.response.StockResponseDto;
import com.escuelajavag4.inventory_service.model.entity.Stock;
import com.escuelajavag4.inventory_service.repository.StockRepository;
import com.escuelajavag4.inventory_service.service.StockService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;


    @Override
    public List<StockResponseDto> getListStock() {
        List<Stock> stocks = stockRepository.findAll();
        return stocks.stream().map(stockMapper::toResponseDto).toList();
    }

    @Override
    public List<StockResponseDto> getStockById(Long productId) {
        List<Stock> stockList  = stockRepository.findAllByProductId(productId).stream().toList();
        return stockMapper.toResponseListDto(stockList);
    }

    @Override
    public StockResponseDto saveStock(StockCreateRequestDto stockCreateRequestDto) {

        if (stockRepository.existsByProductIdAndWarehouseId(
                stockCreateRequestDto.getProductId(),
                stockCreateRequestDto.getWarehouseId()
        )) {
            throw new DuplicateResourceException(
                    "Stock",
                    "productId + warehouseId",
                    stockCreateRequestDto.getProductId() + " - " + stockCreateRequestDto.getWarehouseId()
            );
        }

        if (stockCreateRequestDto.getAvailable() <= 0) {
            throw new IllegalArgumentException("El campo 'available' debe ser mayor a 0");
        }

        Stock stock = stockMapper.toEntity(stockCreateRequestDto);
        Stock savedStock = stockRepository.save(stock);

        return stockMapper.toResponseDto(savedStock);
    }

    @Override
    public StockReservedResponseDto reserveStock(Long productId, int cantidad) {
        List<Stock> stockList = stockRepository.findAllByProductId(productId).stream().toList();

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reservar debe ser mayor a 0");
        }

        int restante = cantidad;
        for (Stock stock : stockList) {
            if (restante  <= 0) break;

            int disponible = stock.getAvailable();
            int aReservar = Math.min(disponible, restante);

            stock.setAvailable(disponible - aReservar);
            stock.setReserved(stock.getReserved() + aReservar);

            restante -= aReservar;
            stockRepository.save(stock);
        }

        if (restante > 0) {
            throw new IllegalArgumentException("No hay suficiente stock total para reservar");
        }

        StockReservedResponseDto stockReservedResponseDto = new StockReservedResponseDto();
        stockReservedResponseDto.setStatus("RESERVED");
        stockReservedResponseDto.setReservationId(UUID.randomUUID().toString());
        stockReservedResponseDto.setExpiresAt(LocalDateTime.now().plusDays(2));

        return stockReservedResponseDto;
    }

    @Override
    public boolean validateStock(Long productId, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a validar debe ser mayor a 0");
        }
        List<Stock> stockList = stockRepository.findAllByProductId(productId).stream().toList();

        if (stockList.isEmpty()) {
            return false;
        }

        int totalDisponible = stockList.stream()
                .mapToInt(Stock::getAvailable)
                .sum();
        return totalDisponible >= cantidad;
    }
}
