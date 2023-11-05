package com.mjpancheri.shortenerurl.core.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ShortenerUrlExceptionResponse(String message, LocalDateTime date) {
}
