package com.mjpancheri.shortenerurl.core.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ShortenerUrlExceptionHandler {

    @ExceptionHandler(ShortenerUrlException.class)
    public ResponseEntity<ShortenerUrlExceptionResponse> handlerShortenerUrlException(ShortenerUrlException exception) {
        ShortenerUrlExceptionResponse res = new ShortenerUrlExceptionResponse(exception.getMessage(),
                LocalDateTime.now());

        return new ResponseEntity<>(res, new HttpHeaders(), exception.getStatus());
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<ShortenerUrlExceptionResponse> handleInternalServerErrorException(
            HttpServerErrorException.InternalServerError exception) {
        ShortenerUrlExceptionResponse res = new ShortenerUrlExceptionResponse(exception.getMessage(),
                LocalDateTime.now());

        return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ShortenerUrlExceptionResponse> handleInputException(
            MethodArgumentNotValidException exception) {
        StringBuilder message = new StringBuilder();
        exception.getFieldErrors()
                .forEach(err -> {
                    message.append(err.getField());
                    message.append(" -> error: ");
                    message.append(err.getDefaultMessage());
                });
        ShortenerUrlExceptionResponse res = new ShortenerUrlExceptionResponse(
                String.format("Invalid argument for %s", message), LocalDateTime.now());

        return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
