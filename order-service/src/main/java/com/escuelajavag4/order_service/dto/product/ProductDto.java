package com.escuelajavag4.order_service.dto.product;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductDto {
    private Long id;
    private String code;
    private String name;
    private BigDecimal price;
    private Boolean active;
}
