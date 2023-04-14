package com.fundquest.assessment.lib.helpers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response {
    private Boolean success;
    private String message;
    private HttpStatus status;
    private Object data;

    public ResponseEntity<Object> entity() {
        if (status == null)
            setStatus(HttpStatus.OK);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", success);

        if (message != null) {
            body.put("message", message);
        }

        body.put("data", data);

        return ResponseEntity.status(status.value()).body(body);
    }

    public static ResponseEntity<Object> of(Object data) {
        return Response.builder()
                .success(true)
                .data(data)
                .status(HttpStatus.OK)
                .build()
                .entity();
    }

    public static ResponseEntity<Object> of(Page<?> data) {
        return Response.of(PaginatedResult.from("entries", data));
    }

    public static ResponseEntity<Object> of(Exception exception) {
        return Response.from(exception).entity();
    }

    public static ResponseEntity<Object> of(Exception exception, HttpStatus status) {
        return Response.from(exception, status).entity();
    }

    public static Response from(Exception exception) {
        return Response.from(exception, HttpStatus.BAD_REQUEST);
    }

    public static Response from(Exception exception, HttpStatus status) {
        return Response.builder()
                .message(exception.getMessage())
                .success(false)
                .status(status)
                .data(null)
                .build();
    }
}
