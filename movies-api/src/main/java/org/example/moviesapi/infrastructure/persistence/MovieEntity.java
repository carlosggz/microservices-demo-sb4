package org.example.moviesapi.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MovieEntity {
    @Id
    @Column(length = 50, updatable = false, nullable = false )
    private String id;

    @Column(length = 500, nullable = false )
    private String title;

    private int releaseYear;

    @Column(length = 20, nullable = false )
    private String imdb;

    @Builder.Default
    private int likes = 0;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @EqualsAndHashCode.Exclude
    private LocalDateTime createdAt;

    @LastModifiedDate
    @EqualsAndHashCode.Exclude
    private LocalDateTime updatedAt;
}

