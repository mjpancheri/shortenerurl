package com.mjpancheri.shortenerurl.core.exception;

public class InvalidUrlException extends ShortenerUrlException {
    public InvalidUrlException() {
        super("The URL must start with HTTP");
    }
}
