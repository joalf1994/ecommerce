package com.escuelajavag4.catalogservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CategoryResponseDto {
    private Long id;
    private String name;
    private Boolean active;
    private Set<ProductResponseDto> products;
}
