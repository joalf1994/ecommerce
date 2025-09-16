package com.escuelajavag4.notification_service.service.impl;

import com.escuelajavag4.notification_service.model.dto.request.OrderConfirmedEventDto;
import com.escuelajavag4.notification_service.model.dto.response.NotificationDto;
import com.escuelajavag4.notification_service.model.entity.NotificationEntity;
import com.escuelajavag4.notification_service.repository.NotificationRepository;
import com.escuelajavag4.notification_service.service.INotificationService;
import com.escuelajavag4.notification_service.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Instant;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public void processOrderConfirmedEvent(OrderConfirmedEventDto orderConfirmedEventDto) {
        log.info("Recibido evento de confirmación de pedido: {}", orderConfirmedEventDto.getOrderId());

        // Simula el envío de la notificación (ej. a través de un servicio de email)
        simulateEmailSending(orderConfirmedEventDto.getOrderId());

        // Crea la entidad de notificación
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setOrderId(orderConfirmedEventDto.getOrderId());
        notificationEntity.setStatus(orderConfirmedEventDto.getStatus());
        notificationEntity.setChannel("EMAIL");
        notificationEntity.setSentAt(Instant.now());

        // Guarda en la base de datos
        notificationRepository.save(notificationEntity);
        log.info("Notificación guardada para el pedido: {}", orderConfirmedEventDto.getOrderId());
    }

    @Override
    public NotificationDto getNotificationByOrderId(String orderId) {
        return notificationRepository.findByOrderId(orderId)
                .map(notificationMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
    }

    private void simulateEmailSending(String orderId) {
        log.info("Simulando envío de email para el pedido: {}", orderId);
    }
}
