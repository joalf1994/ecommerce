package com.escuelajavag4.inventory_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockSaveRequestDto {

    private Long productId;
    private Long warehouseId;
    private int available;
    private int reserved;

}
