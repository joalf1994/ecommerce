package com.escuelajavag4.catalogservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String code;
    private String name;
    private String shortDescription;
    private BigDecimal price;
    private Boolean active;
    private Long categoryId;
    private Long marcaId;
}
