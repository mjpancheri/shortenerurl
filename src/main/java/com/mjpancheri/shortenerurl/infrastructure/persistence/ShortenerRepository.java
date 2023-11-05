package com.mjpancheri.shortenerurl.infrastructure.persistence;

import com.mjpancheri.shortenerurl.core.shortener.Shortener;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortenerRepository extends JpaRepository<Shortener, String> {
}
