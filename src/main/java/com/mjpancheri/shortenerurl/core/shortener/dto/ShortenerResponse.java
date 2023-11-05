package com.mjpancheri.shortenerurl.core.shortener.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ShortenerResponse(String id, String url, long count, List<ConsumerResponse> consumers,
                                LocalDateTime lastAccessed, LocalDateTime createdAt) {
}
