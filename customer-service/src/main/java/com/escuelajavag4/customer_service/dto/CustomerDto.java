package com.escuelajavag4.customer_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {
    private Long id;

    private String email;

    private String fullName;

    private String level;
}
