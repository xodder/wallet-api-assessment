package com.fundquest.assessment.lib.helpers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fundquest.assessment.lib.exception.PlatformException;

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

        if (message != null)
            body.put("message", message);

        if (data != null)
            body.put("data", data);

        return ResponseEntity.status(status.value()).body(body);
    }

    public static Response from(Object data) {
        return Response.builder()
                .success(true)
                .data(data)
                .status(HttpStatus.OK)
                .build();
    }

    public static Response from(Exception exception) {
        return Response.from(exception, HttpStatus.BAD_REQUEST);
    }

    public static Response from(Exception exception, HttpStatus status) {
        Object data = null;

        if (exception instanceof PlatformException __exception) {
            status = __exception.getStatus();
            data = __exception.getMeta();
        }

        return Response.builder()
                .message(exception.getMessage())
                .success(false)
                .status(status)
                .data(data)
                .build();
    }

    public static ResponseEntity<Object> of(Object data) {
        return Response.from(data).entity();
    }

    public static ResponseEntity<Object> of(Page<?> data) {
        return Response.named(data, "entities");
    }

    public static ResponseEntity<Object> of(Exception exception) {
        return Response.from(exception).entity();
    }

    public static ResponseEntity<Object> of(Exception exception, HttpStatus status) {
        return Response.from(exception, status).entity();
    }

    // alias the supplied record inside the data Object
    public static ResponseEntity<Object> named(Object data, String name) {
        Map<String, Object> namedData = new LinkedHashMap<>();
        namedData.put(name, data);

        return Response.of(namedData);
    }

    public static ResponseEntity<Object> named(Page<?> data, String name) {
        return Response.of(PaginatedResult.from(name, data));
    }

}
