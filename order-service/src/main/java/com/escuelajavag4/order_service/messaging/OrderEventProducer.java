package com.escuelajavag4.order_service.messaging;

import com.escuelajavag4.order_service.dto.kafka.OrderCompletedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCompletedEventDto> kafkaTemplate;

    public void emisorCompletedEvent(OrderCompletedEventDto orderCompletedEventDto) {
        kafkaTemplate.send("order-created", orderCompletedEventDto);
    }
}
