package org.example.moviesapi.application;

import lombok.RequiredArgsConstructor;
import org.example.moviesapi.domain.components.MoviesStore;
import org.example.moviesapi.domain.models.MovieDto;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetailsMovieService {
    private final MoviesStore moviesStore;

    public Optional<MovieDto> getMovieById(@NonNull String id) {
        Assert.isTrue(StringUtils.isNotBlank(id), "id must not be null or empty");

        return moviesStore.getMovie(id);
    }
}
