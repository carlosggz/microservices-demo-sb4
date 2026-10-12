package org.example.moviesapi.infrastructure.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesapi.application.*;
import org.example.moviesapi.domain.exceptions.MovieAlreadyExistsException;
import org.example.moviesapi.domain.exceptions.UnknownMovieException;
import org.example.moviesapi.domain.models.MovieDto;
import org.example.moviesapi.domain.models.MovieInfoDto;
import org.example.moviesapi.domain.models.MoviesFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/movies")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MoviesController {

    private final AddMoviesService addMoviesService;
    private final DeleteMoviesService deleteMoviesService;
    private final DetailsMovieService movieDetailService;
    private final ListMoviesService moviesListService;
    private final UpdateMoviesService updateMoviesService;
    private final LikeMoviesService likeMoviesService;

    @GetMapping
    @Tag(name = "List Movies",
            description = "Get a list of movies using a filter. If filter is not provided, all movies will be returned.")
    public List<MovieInfoDto> getMovies(@ModelAttribute MoviesFilter filter) {
        return moviesListService.getMoviesList(filter);
    }

    @GetMapping("/{id}")
    @Tag(name = "Movie details",
            description = "Get details of a specific movie using the id. If the movie is not found, a 404 response will be returned.")
    public ResponseEntity<MovieDto> getDetails(@PathVariable("id") String id) {
        return movieDetailService
                .getMovieById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Tag(name = "Add movie",
            description = "Add a new movie. If a movie with the same id already exists, a 400 response will be returned.")
    public ResponseEntity<MovieDto> add(@RequestBody MovieDto movieDto) {
        try {
            addMoviesService.addMovie(movieDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(movieDto);
        } catch (MovieAlreadyExistsException e) {
            log.info("Movie already exists: {}", movieDto.id());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error occurred while adding movie: {}. Error: {}", movieDto.id(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    @Tag(name = "Update movie",
            description = "Update an existing movie. If the movie does not exist, a 404 response will be returned.")
    public ResponseEntity<MovieDto> update(@RequestBody MovieDto movieDto) {
        try {
            updateMoviesService.updateMovie(movieDto);
            return ResponseEntity.ok(movieDto);
        } catch (UnknownMovieException e) {
            log.info("Movie does not exists: {}", movieDto.id());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error occurred while updating movie: {}. Error: {}", movieDto.id(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Tag(name = "Delete movie",
            description = "Delete an existing movie. If the movie does not exist, a 404 response will be returned.")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        try {
            deleteMoviesService.deleteMovie(id);
            return ResponseEntity.ok().build();
        } catch (UnknownMovieException e) {
            log.error("Movie not found: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error occurred while deleting movie: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("like/{id}")
    @Tag(name = "Like movie",
            description = "Like an existing movie. If the movie does not exist, a 404 response will be returned.")
    public ResponseEntity<?> like(@PathVariable String id) {
        try {
            likeMoviesService.likeMovie(id);
            return ResponseEntity.ok().build();
        } catch (UnknownMovieException e) {
            log.error("Movie not found: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error occurred while liking movie: {}. Error: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
