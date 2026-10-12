package org.example.moviesapi.application;

import lombok.RequiredArgsConstructor;
import org.example.moviesapi.domain.components.MoviesStore;
import org.example.moviesapi.domain.exceptions.MovieAlreadyExistsException;
import org.example.moviesapi.domain.models.MovieDto;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class AddMoviesService {

    private final MoviesStore moviesStore;

    public void addMovie(@NonNull MovieDto movieDto) throws MovieAlreadyExistsException {
        Assert.notNull(movieDto, "movieDto must not be null");

        if (moviesStore.existsMovie(movieDto.id())) {
            throw new MovieAlreadyExistsException("Movie with id " + movieDto.id() + " already exists");
        }

        moviesStore.saveMovie(movieDto);
    }
}
