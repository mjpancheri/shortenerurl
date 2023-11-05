package com.mjpancheri.shortenerurl.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ShortenerUrlException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public ShortenerUrlException(String message) {
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}
