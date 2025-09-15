package com.escuelajavag4.inventory_service.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockCreateRequestDto {

    private Long productId;
    private Long warehouseId;
    private int available;

}
