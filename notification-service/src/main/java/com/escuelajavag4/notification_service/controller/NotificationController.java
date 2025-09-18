package com.escuelajavag4.notification_service.controller;

import com.escuelajavag4.notification_service.model.dto.request.OrderConfirmedEventDto;
import com.escuelajavag4.notification_service.model.dto.response.NotificationDto;
import com.escuelajavag4.notification_service.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final INotificationService iNotificationService;

    @GetMapping("/{orderId}")
    public ResponseEntity<NotificationDto> getNotificationByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(iNotificationService.getNotificationByOrderId(orderId));
    }

}
