package com.escuelajavag4.notification_service.service;

import com.escuelajavag4.notification_service.model.dto.request.OrderConfirmedEventDto;
import com.escuelajavag4.notification_service.model.dto.response.NotificationDto;

public interface INotificationService {
    void processOrderConfirmedEvent(OrderConfirmedEventDto orderConfirmedEventDto);
    NotificationDto getNotificationByOrderId(String orderId);
}
