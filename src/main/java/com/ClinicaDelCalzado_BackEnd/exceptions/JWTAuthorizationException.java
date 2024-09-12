package com.ClinicaDelCalzado_BackEnd.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTAuthorizationException implements AuthenticationEntryPoint {

    private static final HttpStatus httpStatusUnauthorized = HttpStatus.UNAUTHORIZED;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpStatusUnauthorized.value());
        String jsonResponse = String.format(
                "{\"status\": %d,\"error\": \"%s\", \"message\": \"%s\"}",
                httpStatusUnauthorized.value(),
                httpStatusUnauthorized.name(),
                authException.getMessage()
        );

        response.getWriter().write(jsonResponse);
    }
}
