package com.fundquest.assessment.lib.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.fundquest.assessment.lib.helpers.HashMapBuilder;
import com.fundquest.assessment.lib.helpers.Response;

@ControllerAdvice
public class PlatformControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException exception) {
        // build validation data field
        Map<String, String> fields = new LinkedHashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fields.put(fieldName, errorMessage);
        });

        Response response = Response.from(exception, HttpStatus.BAD_REQUEST);

        response.setData(new HashMapBuilder<>()
                .entry("data", fields)
                .build());

        return response.entity();
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> genericException(Exception exception, WebRequest request) {
        return Response.of(exception);
    }
}
