package com.escuelajavag4.catalogservice.controller.handler;

import com.escuelajavag4.catalogservice.model.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,Class<? extends HttpMessageConverter<?>> converterType) {

        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        HttpServletResponse servletResponse = ((org.springframework.http.server.ServletServerHttpResponse) response).getServletResponse();
        int statusCode = servletResponse.getStatus();

        if (body instanceof ApiResponse) {
            return body;
        }

        if (body == null) {
            return ApiResponse.builder()
                    .status(statusCode != 0 ? statusCode : HttpStatus.NO_CONTENT.value())
                    .data(null)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        return ApiResponse.builder()
                .status(statusCode != 0 ? statusCode : HttpStatus.OK.value())
                .data(body)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
