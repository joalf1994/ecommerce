package com.escuelajavag4.inventory_service.model.dto.request;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockReservedResponseDto {
    String status;
    String reservationId;
    LocalDateTime expiresAt;
}
