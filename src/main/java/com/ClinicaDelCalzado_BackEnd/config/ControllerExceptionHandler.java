package com.ClinicaDelCalzado_BackEnd.config;

import com.ClinicaDelCalzado_BackEnd.exceptions.ApiError;
import com.ClinicaDelCalzado_BackEnd.exceptions.ApiException;
import com.ClinicaDelCalzado_BackEnd.exceptions.BadRequestException;
import com.newrelic.api.agent.NewRelic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;

/**
 * Basic handling for exceptions.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * Handler for external API exceptions.
     *
     * @param e the exception thrown during a request to external API.
     * @return {@link ResponseEntity} with status code and description provided for the handled exception.
     */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ApiError> handleApiException(ApiException e) {
        Integer statusCode = e.getStatusCode();
        boolean expected = HttpStatus.INTERNAL_SERVER_ERROR.value() > statusCode;
        NewRelic.noticeError(e, expected);
        if (expected) {
            LOGGER.warn("Internal Api warn. Status Code: " + statusCode, e);
        } else {
            LOGGER.error("Internal Api error. Status Code: " + statusCode, e);
        }

        ApiError apiError = new ApiError(e.getCode(), e.getDescription(), statusCode);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    /**
     * Handler for internal exceptions.
     *
     * @param e the exception thrown during request processing.
     * @return {@link ResponseEntity} with 500 status code and description indicating an internal error.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiError> handleUnknownException(Exception e) {
        LOGGER.error("Internal error", e);
        NewRelic.noticeError(e);

        ApiError apiError =
                new ApiError(
                        "internal_error", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException e) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.name(), e.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badRequest(BadRequestException e) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.name(), e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
