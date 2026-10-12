package org.example.moviesapi.application;

import lombok.RequiredArgsConstructor;
import org.example.moviesapi.domain.components.MoviesStore;
import org.example.moviesapi.domain.exceptions.UnknownMovieException;
import org.example.moviesapi.domain.models.MovieDto;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class UpdateMoviesService {
    private final MoviesStore moviesStore;

    public void updateMovie(@NonNull MovieDto movieDto) throws UnknownMovieException {
        Assert.notNull(movieDto, "movieDto must not be null");

        if (!moviesStore.existsMovie(movieDto.id())) {
            throw new UnknownMovieException("Movie with id " + movieDto.id() + " not found");
        }

        moviesStore.saveMovie(movieDto);
    }
}
