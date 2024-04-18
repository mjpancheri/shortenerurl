package com.mjpancheri.shortenerurl.application.rest;

import com.mjpancheri.shortenerurl.application.service.ShortenerService;
import com.mjpancheri.shortenerurl.core.shortener.Shortener;
import com.mjpancheri.shortenerurl.core.shortener.dto.FullUrlResponse;
import com.mjpancheri.shortenerurl.core.shortener.dto.ShortUrlResponse;
import com.mjpancheri.shortenerurl.core.shortener.dto.ShortenerDto;
import com.mjpancheri.shortenerurl.core.shortener.dto.ShortenerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/shorteners")
public class ShortenerController {

    private final ShortenerService service;

    @PostMapping("/")
    public ResponseEntity<ShortUrlResponse> create(@RequestBody @Valid ShortenerDto body) {
        Shortener shortener = service.save(body);

        return ResponseEntity.created(shortener.uri()).body(shortener.shortUrl());
    }

    @GetMapping("/{id}/short")
    public ResponseEntity<FullUrlResponse> getUrl(@PathVariable("id") String id,
                                                  @RequestHeader(name = "user-agent") String userAgent) {
        Shortener shortener = service.getUrl(id, userAgent);

        return ResponseEntity.ok(shortener.fullUrl());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShortenerResponse> getOne(@PathVariable("id") String id) {
        Shortener shortener = service.find(id);

        return ResponseEntity.ok(shortener.convertToWithConsumer());
    }

    @GetMapping("/")
    public ResponseEntity<List<ShortenerResponse>> listAll() {
        List<Shortener> shorteners = service.list();

        return ResponseEntity.ok(shorteners.stream().map(Shortener::convertTo).toList());
    }
}
