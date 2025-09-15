package com.escuelajavag4.catalogservice.controller.handler;

import com.escuelajavag4.catalogservice.model.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
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

        HttpServletResponse servletResponse =
                ((ServletServerHttpResponse) response).getServletResponse();
        int statusCode = servletResponse.getStatus();

        if (statusCode >= 200 && statusCode < 300) {
            if (body instanceof ApiResponse) {
                return body;
            }

            String methodName = returnType.getMethod().getName().toLowerCase();
            String prefix;
            if (methodName.startsWith("get")) prefix = "get";
            else if (methodName.startsWith("create")) prefix = "create";
            else if (methodName.startsWith("save")) prefix = "save";
            else if (methodName.startsWith("update")) prefix = "update";
            else if (methodName.startsWith("delete")) prefix = "delete";
            else prefix = "other";


            String message;
            switch (prefix) {
                case "get" -> message = "Resource retrieved successfully";
                case "create", "save" -> message = "Resource created successfully";
                case "update" -> message = "Resource updated successfully";
                case "delete" -> message = "Resource deleted successfully";
                default -> message = "Operation completed successfully";
            }

            return ApiResponse.builder()
                    .status(statusCode)
                    .message(message)
                    .data(body)
                    .build();
        }

        return body;
    }

}
