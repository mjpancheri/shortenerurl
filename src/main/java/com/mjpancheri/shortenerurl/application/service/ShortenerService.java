package com.mjpancheri.shortenerurl.application.service;

import com.mjpancheri.shortenerurl.core.exception.ResourceNotFoundException;
import com.mjpancheri.shortenerurl.core.shortener.Consumer;
import com.mjpancheri.shortenerurl.core.shortener.Shortener;
import com.mjpancheri.shortenerurl.core.shortener.dto.FullUrlResponse;
import com.mjpancheri.shortenerurl.core.shortener.dto.ShortenerDto;
import com.mjpancheri.shortenerurl.core.exception.InvalidUrlException;
import com.mjpancheri.shortenerurl.infrastructure.persistence.ShortenerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ShortenerService {

    private final ShortenerRepository repository;

    @Transactional
    public Shortener save(ShortenerDto dto) {
        if (!dto.url().toLowerCase().startsWith("http://") && !dto.url().toLowerCase().startsWith("https://")) {
            throw new InvalidUrlException();
        }

        return repository.save(Shortener.from(dto));
    }

    @Transactional
    public Shortener getUrl(String id, String userAgent) {
        Shortener shortener = repository.findById(id).orElseThrow(ResourceNotFoundException::new);
        shortener.setCount(shortener.getCount() + 1);
        shortener.setLastAccessed(LocalDateTime.now());
        Consumer consumer = new Consumer(userAgent, shortener);
        var costumers = shortener.getConsumers();
        costumers.add(consumer);
        shortener.setConsumers(costumers);
        repository.save(shortener);

        return shortener;
    }

    public Shortener find(String id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public List<Shortener> list() {
        return repository.findAll();
    }
}
