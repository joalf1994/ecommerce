package com.escuelajavag4.notification_service.controller;

import com.escuelajavag4.notification_service.model.dto.request.OrderConfirmedEventDto;
import com.escuelajavag4.notification_service.model.dto.response.NotificationDto;
import com.escuelajavag4.notification_service.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final INotificationService notificationService;

    @GetMapping("/{orderId}")
    public NotificationDto getNotificationByOrderId(@PathVariable String orderId) {
        return notificationService.getNotificationByOrderId(orderId);
    }

    @PostMapping
    public void processOrderConfirmedEvent(@RequestBody  OrderConfirmedEventDto orderConfirmedEventDto){
        notificationService.processOrderConfirmedEvent(orderConfirmedEventDto);
    }
}
