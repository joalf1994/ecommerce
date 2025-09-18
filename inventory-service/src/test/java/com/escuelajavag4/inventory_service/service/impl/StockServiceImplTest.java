package com.escuelajavag4.inventory_service.service.impl;

import com.escuelajavag4.inventory_service.exception.DuplicateResourceException;
import com.escuelajavag4.inventory_service.mapper.StockMapper;
import com.escuelajavag4.inventory_service.model.dto.request.StockCreateRequestDto;
import com.escuelajavag4.inventory_service.model.dto.request.StockReservedResponseDto;
import com.escuelajavag4.inventory_service.model.dto.response.StockResponseDto;
import com.escuelajavag4.inventory_service.model.entity.Stock;
import com.escuelajavag4.inventory_service.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockServiceImpl stockService;

    private Stock stock;
    private StockResponseDto stockDto;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setStockId(1L);
        stock.setProductId(100L);
        stock.setWarehouseId(200L);
        stock.setAvailable(10);
        stock.setReserved(0);

        stockDto = new StockResponseDto();
        stockDto.setStockId(1L);
        stockDto.setProductId(100L);
        stockDto.setWarehouseId(200L);
        stockDto.setAvailable(10);
        stockDto.setReserved(0);
    }
    @Test
    void saveStock_ok() {
        StockCreateRequestDto req = new StockCreateRequestDto();
        req.setProductId(100L);
        req.setWarehouseId(200L);
        req.setAvailable(10);

        when(stockRepository.existsByProductIdAndWarehouseId(100L, 200L)).thenReturn(false);
        when(stockMapper.toEntity(req)).thenReturn(stock);
        when(stockRepository.save(stock)).thenReturn(stock);
        when(stockMapper.toResponseDto(stock)).thenReturn(stockDto);

        StockResponseDto result = stockService.saveStock(req);

        assertNotNull(result);
        assertEquals(100L, result.getProductId());
        verify(stockRepository).save(stock);
    }

    @Test
    void saveStock_duplicado() {
        StockCreateRequestDto req = new StockCreateRequestDto();
        req.setProductId(100L);
        req.setWarehouseId(200L);
        req.setAvailable(10);

        when(stockRepository.existsByProductIdAndWarehouseId(100L, 200L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> stockService.saveStock(req));
    }

    @Test
    void saveStock_availableInvalido() {
        StockCreateRequestDto req = new StockCreateRequestDto();
        req.setProductId(100L);
        req.setWarehouseId(200L);
        req.setAvailable(0);

        assertThrows(IllegalArgumentException.class, () -> stockService.saveStock(req));
    }

    @Test
    void getStockById_ok() {
        when(stockRepository.findAllByProductId(100L)).thenReturn(List.of(stock));
        when(stockMapper.toResponseListDto(List.of(stock))).thenReturn(List.of(stockDto));

        List<StockResponseDto> result = stockService.getStockById(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.getFirst().getProductId());
        verify(stockRepository).findAllByProductId(100L);
    }



    @Test
    void reserveStock_ok() {
        when(stockRepository.findAllByProductId(100L)).thenReturn(List.of(stock));

        StockReservedResponseDto result = stockService.reserveStock(100L, 5);

        assertNotNull(result);
        assertEquals("RESERVED", result.getStatus());
        assertEquals(5, stock.getAvailable());
        assertEquals(5, stock.getReserved());
        verify(stockRepository).save(stock);
    }


    @Test
    void reserveStock_cantidadInvalida() {
        assertThrows(IllegalArgumentException.class, () -> stockService.reserveStock(100L, 0));
    }

    @Test
    void reserveStock_noHayStockSuficiente() {
        stock.setAvailable(3);
        when(stockRepository.findAllByProductId(100L)).thenReturn(List.of(stock));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> stockService.reserveStock(100L, 10));

        assertEquals("No hay suficiente stock total para reservar", ex.getMessage());
    }
    @Test
    void getListStock_ok() {
        Stock stock = new Stock();
        stock.setProductId(100L);
        stock.setWarehouseId(1L);

        StockResponseDto dto = new StockResponseDto();
        dto.setProductId(100L);
        dto.setWarehouseId(1L);

        when(stockRepository.findAll()).thenReturn(List.of(stock));
        when(stockMapper.toResponseDto(stock)).thenReturn(dto);

        List<StockResponseDto> result = stockService.getListStock();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.getFirst().getProductId());
        verify(stockRepository).findAll();
    }

    @Test
    void getListStock_empty() {
        when(stockRepository.findAll()).thenReturn(List.of());

        List<StockResponseDto> result = stockService.getListStock();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockRepository).findAll();
    }

    @Test
    void reserveStock_variosAlmacenes() {
        Stock stock1 = new Stock();
        stock1.setAvailable(3);
        stock1.setReserved(0);

        Stock stock2 = new Stock();
        stock2.setAvailable(5);
        stock2.setReserved(0);

        when(stockRepository.findAllByProductId(100L)).thenReturn(List.of(stock1, stock2));

        StockReservedResponseDto result = stockService.reserveStock(100L, 7);

        assertEquals("RESERVED", result.getStatus());
        assertEquals(0, stock1.getAvailable());
        assertEquals(3, stock1.getReserved());
        assertEquals(1, stock2.getAvailable());
        assertEquals(4, stock2.getReserved());
        verify(stockRepository, times(2)).save(any(Stock.class));
    }


    @Test
    void validateStock_ok_true() {
        stock.setAvailable(10);
        when(stockRepository.findAllByProductId(100L)).thenReturn(List.of(stock));

        boolean result = stockService.validateStock(100L, 5);

        assertTrue(result);
        verify(stockRepository).findAllByProductId(100L);
    }

    @Test
    void validateStock_ok_false() {
        stock.setAvailable(3);
        when(stockRepository.findAllByProductId(100L)).thenReturn(List.of(stock));

        boolean result = stockService.validateStock(100L, 5);

        assertFalse(result);
    }

    @Test
    void validateStock_listaVacia() {
        when(stockRepository.findAllByProductId(100L)).thenReturn(List.of());

        boolean result = stockService.validateStock(100L, 5);

        assertFalse(result);
    }

    @Test
    void validateStock_cantidadInvalida() {
        assertThrows(IllegalArgumentException.class, () -> stockService.validateStock(100L, 0));
    }
}
