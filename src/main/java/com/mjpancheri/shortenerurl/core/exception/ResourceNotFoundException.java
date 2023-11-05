package com.mjpancheri.shortenerurl.core.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ShortenerUrlException {
    public ResourceNotFoundException() {
        super("Resource not found", HttpStatus.NOT_FOUND);
    }
}
