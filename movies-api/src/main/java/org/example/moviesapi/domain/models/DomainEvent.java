package org.example.moviesapi.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public abstract class DomainEvent {

    private String movieId;
    private String eventName;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime occurrenceDate;

    protected DomainEvent(String movieId, String eventName) {
        this.movieId = movieId;
        this.occurrenceDate = LocalDateTime.now();
        this.eventName = eventName;
    }
}
