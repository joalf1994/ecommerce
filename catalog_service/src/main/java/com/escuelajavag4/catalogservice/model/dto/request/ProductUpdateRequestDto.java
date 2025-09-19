package com.escuelajavag4.catalogservice.model.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductUpdateRequestDto {

    @Size(min = 2, max = 50, message = "El código debe tener entre 2 y 50 caracteres")
    private String code;

    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción corta no puede exceder 500 caracteres")
    private String shortDescription;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 enteros y 2 decimales")
    private BigDecimal price;

    private Long categoryId;

    private Long marcaId;

    private Boolean active;
}
