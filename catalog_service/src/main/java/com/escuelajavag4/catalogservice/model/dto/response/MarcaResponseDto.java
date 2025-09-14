package com.escuelajavag4.catalogservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarcaResponseDto {
    private Long id;
    private String name;
    private Boolean active;
    private Set<ProductResponseDto> products;
}
