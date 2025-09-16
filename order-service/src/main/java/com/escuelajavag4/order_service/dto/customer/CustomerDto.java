package com.escuelajavag4.order_service.dto.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerDto {
    private Long id;

    private String email;

    private String fullName;

    private String level;
}
