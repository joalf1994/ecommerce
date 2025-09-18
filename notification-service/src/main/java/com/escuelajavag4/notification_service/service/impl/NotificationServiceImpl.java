package com.escuelajavag4.notification_service.service.impl;

import com.escuelajavag4.notification_service.model.dto.request.OrderConfirmedEventDto;
import com.escuelajavag4.notification_service.model.dto.response.NotificationDto;
import com.escuelajavag4.notification_service.model.entity.NotificationEntity;
import com.escuelajavag4.notification_service.repository.NotificationRepository;
import com.escuelajavag4.notification_service.service.INotificationService;
import com.escuelajavag4.notification_service.mapper.NotificationMapper;
import com.escuelajavag4.notification_service.template.EmailTemplates;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import com.resend.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);


    @Value("${resend.api-key}")
    private String resendApiKey;

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
        return notificationRepository.findFirstByOrderIdOrderBySentAtDesc(orderId)
                .map(notificationMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
    }

    private void simulateEmailSending(String orderId) {
        log.info("Simulando envío de email para el pedido: {}", orderId);

        final String htmlBody = EmailTemplates.reservaConfirmada();
        try{
            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions sendEmailRequest = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to("sandrogopher@gmail.com")
                    .subject("Ecommerce G4 Java")
                    .html(htmlBody)
                    .build();

            CreateEmailResponse data = resend.emails().send(sendEmailRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar email");
        }

    }
}
