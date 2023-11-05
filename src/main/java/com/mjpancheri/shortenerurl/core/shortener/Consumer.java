package com.mjpancheri.shortenerurl.core.shortener;

import com.mjpancheri.shortenerurl.core.shortener.dto.ConsumerResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consumers")
public class Consumer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin")
    private String origin;

    @ManyToOne
    @JoinColumn(name = "shortener_id")
    private Shortener shortener;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Consumer(String origin, Shortener shortener) {
        this.origin = origin;
        this.shortener = shortener;
    }

    public ConsumerResponse convertTo() {
        return new ConsumerResponse(this.origin, this.createdAt);
    }
}
