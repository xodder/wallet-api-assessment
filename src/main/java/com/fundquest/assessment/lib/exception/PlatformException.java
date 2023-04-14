package com.fundquest.assessment.lib.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Builder(toBuilder = true)
@Setter
@Getter
public class PlatformException extends Exception {
    private String message;

    @Builder.Default
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    @Singular(value = "metaEntry", ignoreNullCollections = true)
    private Map<String, Object> meta;


    public PlatformException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlatformException(String message) {
        super(message);
    }

    public PlatformException(String message, HttpStatus status, Map<String, Object> meta) {
        super(message);
        this.status = status;
        this.meta = meta;
    }
}
