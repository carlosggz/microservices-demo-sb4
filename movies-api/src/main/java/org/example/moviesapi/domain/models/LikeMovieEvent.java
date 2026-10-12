package org.example.moviesapi.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

@Getter
@Setter
@NoArgsConstructor
public class LikeMovieEvent extends DomainEvent{
    public static final String EVENT_NAME = "movie.like";
    private int likes;

    public LikeMovieEvent(@NonNull MovieDto movie) {
        super(movie.id(), EVENT_NAME);
        this.likes = movie.likes();
    }
}
