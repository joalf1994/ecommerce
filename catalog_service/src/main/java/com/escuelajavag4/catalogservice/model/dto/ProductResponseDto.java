package com.escuelajavag4.catalogservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private Boolean active;

    private CategoryResponseDto category;
    private MarcaResponseDto marca;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
