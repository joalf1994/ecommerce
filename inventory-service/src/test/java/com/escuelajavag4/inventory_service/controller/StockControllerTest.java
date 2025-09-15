package com.escuelajavag4.inventory_service.controller;

import com.escuelajavag4.inventory_service.model.dto.request.StockCreateRequestDto;
import com.escuelajavag4.inventory_service.model.dto.request.StockReservedResponseDto;
import com.escuelajavag4.inventory_service.model.dto.response.StockResponseDto;
import com.escuelajavag4.inventory_service.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockController.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        StockService stockService() {
            return Mockito.mock(StockService.class);
        }
    }
    @Autowired
    private StockService stockService;

    @Test
    void testGetListStock() throws Exception {
        StockResponseDto dto = new StockResponseDto();
        dto.setStockId(1L);
        dto.setProductId(100L);
        dto.setWarehouseId(200L);
        dto.setAvailable(10);
        dto.setReserved(0);
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());

        List<StockResponseDto> responseList = List.of(dto);

        Mockito.when(stockService.getListStock()).thenReturn(responseList);

        mockMvc.perform(get("/inventory/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stockId").value(1L))
                .andExpect(jsonPath("$[0].productId").value(100L))
                .andExpect(jsonPath("$[0].warehouseId").value(200L))
                .andExpect(jsonPath("$[0].available").value(10))
                .andExpect(jsonPath("$[0].reserved").value(0));
    }

    @Test
    void testCreateStock() throws Exception {
        StockCreateRequestDto req = new StockCreateRequestDto();
        req.setProductId(100L);
        req.setWarehouseId(200L);
        req.setAvailable(10);

        StockResponseDto responseDto = new StockResponseDto();
        responseDto.setStockId(1L);
        responseDto.setProductId(100L);
        responseDto.setWarehouseId(200L);
        responseDto.setAvailable(10);
        responseDto.setReserved(0);
        responseDto.setCreatedAt(new Date());
        responseDto.setUpdatedAt(new Date());

        Mockito.when(stockService.saveStock(any(StockCreateRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/inventory/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockId").value(1L))
                .andExpect(jsonPath("$.productId").value(100L))
                .andExpect(jsonPath("$.warehouseId").value(200L))
                .andExpect(jsonPath("$.available").value(10))
                .andExpect(jsonPath("$.reserved").value(0));
    }

    @Test
    void testReserveStock() throws Exception {
        StockReservedResponseDto reservedDto = new StockReservedResponseDto();
        reservedDto.setStatus("RESERVED");
        reservedDto.setReservationId("abc-123");
        reservedDto.setExpiresAt(LocalDateTime.now().plusDays(2));

        Mockito.when(stockService.reserveStock(eq(100L), eq(5)))
                .thenReturn(reservedDto);

        mockMvc.perform(patch("/inventory/reservations/{productId}/reserve", 100L)
                        .param("cantidad", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESERVED"))
                .andExpect(jsonPath("$.reservationId").value("abc-123"));
    }
}
