package com.escuelajavag4.notification_service.messaging;

import com.escuelajavag4.notification_service.model.dto.request.PaymentCompletedEvent;
import com.escuelajavag4.notification_service.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final INotificationService iNotificationService;

    @KafkaListener(topics = "payment-completed-topic", groupId = "notification-service")
    public void onOrderCreated(PaymentCompletedEvent orderConfirmedEventDto) {
        iNotificationService.processOrderConfirmedEvent(orderConfirmedEventDto);
    }

}
