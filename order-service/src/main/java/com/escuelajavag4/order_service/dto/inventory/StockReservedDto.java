package com.escuelajavag4.order_service.dto.inventory;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class StockReservedDto {
    private String status;
    private String reservationId;
    private LocalDateTime expiresAt;
}
