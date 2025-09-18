package com.escuelajavag4.notification_service.service;

import com.escuelajavag4.notification_service.model.dto.request.PaymentCompletedEvent;
import com.escuelajavag4.notification_service.model.dto.response.NotificationDto;

public interface INotificationService {
    void processOrderConfirmedEvent(PaymentCompletedEvent paymentCompletedEvent);
    NotificationDto getNotificationByOrderId(Long orderId);
}
