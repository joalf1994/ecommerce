package com.escuelajavag4.order_service.dto.inventory;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Setter
@Getter
@Builder
public class StockDto {
    private Long stockId;
    private Long productId;
    private Long warehouseId;
    private int available;
    private int reserved;
}
