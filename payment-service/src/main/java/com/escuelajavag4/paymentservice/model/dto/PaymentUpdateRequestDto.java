package com.escuelajavag4.paymentservice.model.dto;

import com.escuelajavag4.paymentservice.model.entity.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentUpdateRequestDto {
    @NotNull(message = "El estado es obligatorio")
    private PaymentStatus status;
}
