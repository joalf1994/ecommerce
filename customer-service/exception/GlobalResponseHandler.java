package com.escuelajavag4.customer_service.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
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

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int statusCode = servletResponse.getStatus();

        if (body instanceof ErrorResponse) {
            return body;
        }

        if (body instanceof ApiResponse) {
            return body;
        }

        if (statusCode == HttpStatus.OK.value()) {
            return ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("OK")
                    .data(body)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        if (statusCode >= 200 && statusCode < 300) {
            return ApiResponse.builder()
                    .status(statusCode)
                    .message("Success")
                    .data(body)
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        return ErrorResponse.builder()
                .status(statusCode)
                .message("Error inesperado")
                .timestamp(LocalDateTime.now())
                .build();
    }
}