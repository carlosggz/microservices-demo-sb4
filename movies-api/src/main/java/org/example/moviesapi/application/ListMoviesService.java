package org.example.moviesapi.application;

import lombok.RequiredArgsConstructor;
import org.example.moviesapi.domain.components.MoviesStore;
import org.example.moviesapi.domain.models.MovieInfoDto;
import org.example.moviesapi.domain.models.MoviesFilter;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListMoviesService {
    private final MoviesStore moviesStore;

    public List<MovieInfoDto> getMoviesList(@Nullable MoviesFilter filter) {
        return moviesStore.getMovies(filter);
    }
}
