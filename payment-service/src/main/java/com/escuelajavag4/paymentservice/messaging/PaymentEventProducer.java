package com.escuelajavag4.paymentservice.messaging;

import com.escuelajavag4.paymentservice.model.dto.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    public void emiter(PaymentCompletedEvent event) {
        kafkaTemplate.send("payment-completed-topic", event);
    }
}