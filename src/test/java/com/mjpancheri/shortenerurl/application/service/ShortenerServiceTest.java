package com.mjpancheri.shortenerurl.application.service;

import com.mjpancheri.shortenerurl.application.rest.builder.ShortenerBuilder;
import com.mjpancheri.shortenerurl.core.exception.InvalidUrlException;
import com.mjpancheri.shortenerurl.core.exception.ResourceNotFoundException;
import com.mjpancheri.shortenerurl.core.shortener.Shortener;
import com.mjpancheri.shortenerurl.infrastructure.persistence.ShortenerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class ShortenerServiceTest {

    @Mock
    private ShortenerRepository repository;
    @InjectMocks
    private ShortenerService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveReturnsNewShortenerWhenInputIsOk() {
        when(repository.save(any(Shortener.class))).thenReturn(ShortenerBuilder.getNewShortener());

        Shortener shortener = service.save(ShortenerBuilder.getShortenerDto());

        assertEquals(ShortenerBuilder.ID, shortener.getId());
        assertEquals(ShortenerBuilder.URL, shortener.getUrl());
        assertEquals(0, shortener.getCount());
        assertEquals(ShortenerBuilder.NOW, shortener.getCreatedAt());
        assertEquals(0, shortener.getConsumers().size());
        assertNull(shortener.getLastAccessed());
    }

    @Test
    void testSaveReturnsErrorWhenInputIsInvalid() {
        assertThrows(InvalidUrlException.class,
                () -> service.save(ShortenerBuilder.getShortenerDtoEmpty()));
    }

    @Test
    void testGetUrlReturnsCorrectlyWhenIdExists() {
        when(repository.findById(anyString())).thenReturn(Optional.of(ShortenerBuilder.getNewShortener()));
        when(repository.save(any(Shortener.class))).thenReturn(ShortenerBuilder.getShortener());

        Shortener shortener = service.getUrl(ShortenerBuilder.ID, ShortenerBuilder.USER_AGENT);

        assertEquals(ShortenerBuilder.ID, shortener.getId());
        assertEquals(ShortenerBuilder.URL, shortener.getUrl());
    }

    @Test
    void testGetUrlReturnsErrorWhenIdNotExists() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getUrl(ShortenerBuilder.ID, ShortenerBuilder.USER_AGENT));
    }

    @Test
    void testFindReturnsShortenerWhenIdExists() {
        when(repository.findById(anyString())).thenReturn(Optional.of(ShortenerBuilder.getShortener()));

        Shortener shortener = service.find(ShortenerBuilder.ID);

        assertEquals(ShortenerBuilder.ID, shortener.getId());
        assertEquals(ShortenerBuilder.URL, shortener.getUrl());
        assertEquals(ShortenerBuilder.COUNT, shortener.getCount());
        assertEquals(ShortenerBuilder.NOW, shortener.getLastAccessed());
        assertEquals(ShortenerBuilder.NOW, shortener.getCreatedAt());
        assertEquals(1, shortener.getConsumers().size());
    }

    @Test
    void testFindReturnsErrorWhenIdNotExists() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.find(ShortenerBuilder.ID));
    }

    @Test
    void testListReturnsCorrectly() {
        when(repository.findAll()).thenReturn(Collections.singletonList(ShortenerBuilder.getShortener()));

        List<Shortener> shorteners = service.list();

        assertEquals(1, shorteners.size());
        assertEquals(ShortenerBuilder.URL, shorteners.get(0).getUrl());
    }
}