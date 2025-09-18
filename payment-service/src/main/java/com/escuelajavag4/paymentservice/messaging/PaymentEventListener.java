package com.escuelajavag4.paymentservice.messaging;

import com.escuelajavag4.paymentservice.model.dto.OrderCompletedEventDto;
import com.escuelajavag4.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final PaymentService paymentService;

    @KafkaListener(topics = "order-created", groupId = "payment-service")
    public void onOrderCreated(OrderCompletedEventDto event) {
        paymentService.cacheOrderEmail(event.getOrderId(), event.getEmail());
        paymentService.createDeuda(event);
    }


}

