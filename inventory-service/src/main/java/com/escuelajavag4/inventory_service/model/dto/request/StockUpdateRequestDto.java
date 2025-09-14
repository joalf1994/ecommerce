package com.escuelajavag4.inventory_service.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StockUpdateRequestDto {
    private Long productId;
    private Long warehouseId;
    private int available;
    private int reserved;
    private Date  updatedAt;
}
