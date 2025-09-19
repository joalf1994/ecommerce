package com.escuelajavag4.paymentservice.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentCreateRequestDto {
    @NotNull(message = "El orderId es obligatorio")
    private Long orderId;

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 0, message = "El monto no puede ser negativo")
    private BigDecimal amount;
}
