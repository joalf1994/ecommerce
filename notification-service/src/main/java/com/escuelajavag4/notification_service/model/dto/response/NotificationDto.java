package com.escuelajavag4.notification_service.model.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class NotificationDto {
    private Long orderId;
    private String status;
    private String channel;
    private Instant sentAt;
}
