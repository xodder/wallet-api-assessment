package com.fundquest.assessment.lib.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class PlatformException extends Exception {
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private Map<String, Object> meta = new LinkedHashMap<>();

    public PlatformException() {
    }

    public PlatformException(String message) {
        super(message);
    }

    public PlatformException withStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public PlatformException withMetaEntry(String key, Object value) {
        meta.put(key, value);
        return this;
    }

    public String getMessage() {
        return super.getMessage();
    }
}
