package com.fundquest.assessment.lib.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class SharedControllerAdvice {

}

// public class ResponseExceptionHandler extends ResponseEntityExceptionHandler
// {

// @Override
// protected ResponseEntity<Object> createResponseEntity(@Nullable Object body,
// HttpHeaders headers,
// HttpStatusCode statusCode, WebRequest request) {
// return super.createResponseEntity(body, headers, statusCode, request);
// }

// }
