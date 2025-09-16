package com.escuelajavag4.notification_service.kafka;

import com.escuelajavag4.notification_service.model.dto.request.OrderConfirmedEventDto;
import com.escuelajavag4.notification_service.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConfirmedConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderConfirmedConsumer.class);
    private final INotificationService notificationService;

    @KafkaListener(topics = "ORDER_CONFIRMED", groupId = "notification-group")
    public void consumeOrderConfirmedEvent(ConsumerRecord<String, OrderConfirmedEventDto> record,
                                           Acknowledgment ack) {
        try {
            OrderConfirmedEventDto event = record.value();
            log.info("Recibiendo evento ORDER_CONFIRMED: {}", event.getOrderId());

            notificationService.processOrderConfirmedEvent(event);

            // Confirma el procesamiento del mensaje
            ack.acknowledge();
            log.info("Evento procesado exitosamente: {}", event.getOrderId());

        } catch (Exception e) {
            log.error("Error procesando evento ORDER_CONFIRMED: {}", e.getMessage());
            // Puedes implementar lógica de reintento o dead-letter queue aquí
        }
    }

}
