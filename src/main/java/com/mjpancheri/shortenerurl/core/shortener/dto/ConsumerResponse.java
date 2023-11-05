package com.mjpancheri.shortenerurl.core.shortener.dto;

import java.time.LocalDateTime;

public record ConsumerResponse(String origin, LocalDateTime createdAt) {
}
