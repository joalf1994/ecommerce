package com.escuelajavag4.paymentservice.messaging;

import com.escuelajavag4.paymentservice.model.dto.OrderCreatedEvent;
import com.escuelajavag4.paymentservice.model.dto.PaymentCreateRequestDto;
import com.escuelajavag4.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final PaymentService paymentService;

    @KafkaListener(topics = "order-created", groupId = "payment-service")
    public void onOrderCreated(OrderCreatedEvent event) {
        PaymentCreateRequestDto dto = new PaymentCreateRequestDto();
        dto.setOrderId(event.getOrderId());
        dto.setAmount(event.getAmount());

        paymentService.create(dto);
    }

}
