package com.mizitoh.webflux.controller.exceptions;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<Mono<StandardError>> duplicateKeyException(DuplicateKeyException ex, ServerHttpRequest request
    ) {
        return ResponseEntity.badRequest()
                .body(Mono.just(
                        StandardError.builder()
                                .timeStamp(now())
                                .status(BAD_REQUEST.value())
                                .error(BAD_REQUEST.getReasonPhrase())
                                .message(ex.getMessage())
                                .path(request.getURI().getPath())
                                .build()));
    }
}
