package org.example.moviesapi.domain.components;

import org.example.moviesapi.domain.models.MovieDto;
import org.example.moviesapi.domain.models.MovieInfoDto;
import org.example.moviesapi.domain.models.MoviesFilter;

import java.util.List;
import java.util.Optional;

public interface MoviesStore {
    boolean existsMovie(String id);

    List<MovieInfoDto> getMovies(MoviesFilter filter);
    Optional<MovieDto> getMovie(String id);
    void saveMovie(MovieDto movieDto);
    void deleteMovie(String id);
}
