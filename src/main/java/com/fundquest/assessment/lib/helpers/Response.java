package com.fundquest.assessment.lib.helpers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Value;

@Value
public class Response {
    private Boolean success;
    private String message;
    private HttpStatus status;
    private Map<String, Object> body;

    public static ResponseEntity<Object> of(Object data) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);

        return ResponseEntity.ok(result);
    }

    public static ResponseEntity<Object> of(Page<?> data) {
        return of(PaginatedResult.from("entries", data));
    }

    public static ResponseEntity<Object> of(Exception exception) {
        HashMap<String, Object> result = new HashMap<>();

        result.put("success", false);
        result.put("message", exception.getMessage());

        return ResponseEntity.ok(result);
    }
}
