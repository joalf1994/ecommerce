package com.escuelajavag4.notification_service.controller;

import com.escuelajavag4.notification_service.model.dto.request.OrderConfirmedEventDto;
import com.escuelajavag4.notification_service.model.dto.response.NotificationDto;
import com.escuelajavag4.notification_service.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServiceImpl notificationServiceImpl;
    private final KafkaTemplate<String,OrderConfirmedEventDto> kafkaTemplate;


    @GetMapping("/{orderId}")
    public NotificationDto getNotificationByOrderId(@PathVariable String orderId) {
        return notificationServiceImpl.getNotificationByOrderId(orderId);
    }

    @PostMapping
    public void processOrderConfirmedEvent(@RequestBody  OrderConfirmedEventDto orderConfirmedEventDto){

        kafkaTemplate.send("notification-created", orderConfirmedEventDto);

        notificationServiceImpl.processOrderConfirmedEvent(orderConfirmedEventDto);
    }
}
