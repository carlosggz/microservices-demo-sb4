package org.example.actorsapi.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record LikeMovieEvent(
        String movieId,
        String eventName,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime occurrenceDate,
        int likes
){
}
