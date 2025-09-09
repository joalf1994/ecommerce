package com.escuelajavag4.inventory_service.dto;

import java.util.Date;

public class StockSaveResponseDto {
    private Long stockId;
    private Long productId;
    private Long warehouseId;
    private int available;
    private int reserved;
    private Date createdAt;
    private Date  updatedAt;
}
