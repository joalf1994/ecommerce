package com.escuelajavag4.notification_service.model.dto.request;

import lombok.Data;


@Data
public class OrderConfirmedEventDto {
    private String orderId;
    private String status;
}
