package com.mjpancheri.shortenerurl.core.shortener.dto;

import jakarta.validation.constraints.NotBlank;

public record ShortenerDto(String url) {
}
