package com.escuelajavag4.notification_service.messaging;


import com.escuelajavag4.notification_service.model.dto.request.OrderConfirmedEventDto;
import com.escuelajavag4.notification_service.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final INotificationService iNotificationService;

    @KafkaListener(topics = "notification-created", groupId = "notification-service")
    public void onOrderCreated(OrderConfirmedEventDto orderConfirmedEventDto) {
        iNotificationService.processOrderConfirmedEvent(orderConfirmedEventDto);
    }

}
