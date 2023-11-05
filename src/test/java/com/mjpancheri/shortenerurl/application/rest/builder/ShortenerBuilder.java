package com.mjpancheri.shortenerurl.application.rest.builder;

import com.mjpancheri.shortenerurl.core.shortener.Consumer;
import com.mjpancheri.shortenerurl.core.shortener.Shortener;
import com.mjpancheri.shortenerurl.core.shortener.dto.ConsumerResponse;
import com.mjpancheri.shortenerurl.core.shortener.dto.ShortenerDto;
import com.mjpancheri.shortenerurl.core.shortener.dto.ShortenerResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShortenerBuilder {

    public static final String ID = "XPTO";
    public static final String URL = "https://url.domain.com";
    public static final int COUNT = 1;
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final String USER_AGENT = "Mozilla bla bla bla";

    public static Shortener getShortener() {
        Consumer consumer = Consumer.builder()
                .origin(USER_AGENT)
                .createdAt(NOW)
                .build();

        return Shortener.builder()
                .id(ID)
                .url(URL)
                .count(COUNT)
                .consumers(Collections.singletonList(consumer))
                .lastAccessed(NOW)
                .createdAt(NOW)
                .build();
    }

    public static Shortener getNewShortener() {
        return Shortener.builder()
                .id(ID)
                .url(URL)
                .count(0)
                .consumers(new ArrayList<>())
                .createdAt(NOW)
                .build();
    }

    public static ShortenerDto getShortenerDto() {
        return new ShortenerDto(URL);
    }

    public static ShortenerDto getShortenerDtoEmpty() {
        return new ShortenerDto("");
    }

}
