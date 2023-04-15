package com.fundquest.assessment.lib.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Builder
@Setter
@Getter
public class PlatformException extends Exception {
    @Builder.Default
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    @Singular(value = "metaEntry", ignoreNullCollections = true)
    private Map<String, Object> meta;
}
