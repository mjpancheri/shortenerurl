package com.mjpancheri.shortenerurl.core.shortener;

import com.mjpancheri.shortenerurl.core.shortener.dto.ConsumerResponse;
import com.mjpancheri.shortenerurl.core.shortener.dto.FullUrlResponse;
import com.mjpancheri.shortenerurl.core.shortener.dto.ShortUrlResponse;
import com.mjpancheri.shortenerurl.core.shortener.dto.ShortenerDto;
import com.mjpancheri.shortenerurl.core.shortener.dto.ShortenerResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mjpancheri.shortenerurl.core.shortener.Utils.generateNanoId;
import static com.mjpancheri.shortenerurl.core.shortener.Utils.getShortUrl;
import static com.mjpancheri.shortenerurl.core.shortener.Utils.getUriResource;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shorteners")
public class Shortener {

    @NotNull
    @Id
    private String id;

    @NotBlank
    @Column(name = "url")
    private String url;

    @Column(name = "count")
    private long count;

    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;

    @OneToMany(mappedBy = "shortener", cascade = CascadeType.MERGE)
    private List<Consumer> consumers = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public static Shortener from(ShortenerDto dto) {
        Shortener shortener = new Shortener();
        shortener.setId(generateNanoId());
        shortener.setUrl(dto.url());
        shortener.setCount(0);

        return shortener;
    }

    public ShortenerResponse convertTo() {
        return new ShortenerResponse(this.id, this.url, this.count, null, this.lastAccessed, this.createdAt);
    }

    public ShortenerResponse convertToWithConsumer() {
        List<ConsumerResponse> list = this.consumers.stream().map(Consumer::convertTo).toList();

        return new ShortenerResponse(this.id, this.url, this.count, list, this.lastAccessed, this.createdAt);
    }

    public ShortUrlResponse shortUrl() {
        return new ShortUrlResponse(getShortUrl(this.id));
    }

    public FullUrlResponse fullUrl() {
        return new FullUrlResponse(this.url);
    }

    public URI uri() {
        return getUriResource(this.id);
    }
}
