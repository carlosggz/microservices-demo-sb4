package org.example.moviesapi.infrastructure.persistence;

import org.example.moviesapi.domain.models.MoviesFilter;
import org.example.moviesapi.utils.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MoviesSpecificationTest extends IntegrationTest {

    @Autowired
    private MoviesRepository moviesRepository;

    private static final List<MovieEntity> MOVIES = List.of(
            MovieEntity.builder()
                    .id("M1")
                    .title("The Matrix")
                    .releaseYear(1999)
                    .imdb("tt0133093")
                    .likes(100)
                    .build(),
            MovieEntity.builder()
                    .id("M2")
                    .title("Inception")
                    .releaseYear(2010)
                    .imdb("tt1375666")
                    .likes(50)
                    .build(),
            MovieEntity.builder()
                    .id("M3")
                    .title("Interstellar")
                    .releaseYear(2014)
                    .imdb("tt0816692")
                    .likes(75)
                    .build(),
            MovieEntity.builder()
                    .id("SW1")
                    .title("Star Wars IV: A New Hope")
                    .releaseYear(1977)
                    .imdb("tt0076759")
                    .likes(20)
                    .build()
    );

    @BeforeEach
    void setUp() {
        moviesRepository.deleteAll();
        moviesRepository.saveAll(MOVIES);
    }

    @Test
    @DisplayName("Should return all movies when filter is null")
    void shouldReturnAllMoviesWhenFilterIsNull() {
        MoviesSpecification specification = new MoviesSpecification(null);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactlyInAnyOrder("M1", "M2", "M3", "SW1");
    }

    @Test
    @DisplayName("Should return all movies when filter has all null properties")
    void shouldReturnAllMoviesWhenFilterIsEmpty() {
        MoviesFilter filter = new MoviesFilter(null, null, null, null, null);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactlyInAnyOrder("M1", "M2", "M3", "SW1");
    }

    @Test
    @DisplayName("Should return all movies when text filter is empty or blank")
    void shouldReturnAllMoviesWhenTextFilterIsBlank() {
        MoviesFilter filter = new MoviesFilter("   ", null, null, null, null);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactlyInAnyOrder("M1", "M2", "M3", "SW1");
    }

    @Test
    @DisplayName("Should filter movies by text matching ID (case insensitive)")
    void shouldFilterMoviesByTextMatchingId() {
        MoviesFilter filter = new MoviesFilter("sw1", null, null, null, null);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactly("SW1");
    }

    @Test
    @DisplayName("Should filter movies by text matching title (case insensitive and partial)")
    void shouldFilterMoviesByTextMatchingTitle() {
        MoviesFilter filter = new MoviesFilter("matrix", null, null, null, null);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactly("M1");
    }

    @Test
    @DisplayName("Should filter movies by text matching imdb code")
    void shouldFilterMoviesByTextMatchingImdb() {
        MoviesFilter filter = new MoviesFilter("0816692", null, null, null, null);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactly("M3");
    }

    @Test
    @DisplayName("Should return empty list when text does not match any movie")
    void shouldReturnEmptyWhenTextDoesNotMatch() {
        MoviesFilter filter = new MoviesFilter("NonExistentText", null, null, null, null);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("Should filter movies by startYear (greater than or equal)")
    void shouldFilterMoviesByStartYear() {
        MoviesFilter filter = new MoviesFilter(null, 2010, null, null, null);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactlyInAnyOrder("M2", "M3");
    }

    @Test
    @DisplayName("Should filter movies by endYear (less than or equal)")
    void shouldFilterMoviesByEndYear() {
        MoviesFilter filter = new MoviesFilter(null, null, 1999, null, null);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactlyInAnyOrder("M1", "SW1");
    }

    @Test
    @DisplayName("Should filter movies by year range (startYear and endYear)")
    void shouldFilterMoviesByYearRange() {
        MoviesFilter filter = new MoviesFilter(null, 1999, 2010, null, null);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactlyInAnyOrder("M1", "M2");
    }

    @Test
    @DisplayName("Should filter movies by startLikes (greater than or equal)")
    void shouldFilterMoviesByStartLikes() {
        MoviesFilter filter = new MoviesFilter(null, null, null, 75, null);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactlyInAnyOrder("M1", "M3");
    }

    @Test
    @DisplayName("Should filter movies by endLikes (less than or equal)")
    void shouldFilterMoviesByEndLikes() {
        MoviesFilter filter = new MoviesFilter(null, null, null, null, 50);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactlyInAnyOrder("M2", "SW1");
    }

    @Test
    @DisplayName("Should filter movies by likes range (startLikes and endLikes)")
    void shouldFilterMoviesByLikesRange() {
        MoviesFilter filter = new MoviesFilter(null, null, null, 50, 75);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactlyInAnyOrder("M2", "M3");
    }

    @Test
    @DisplayName("Should filter movies by combining text, year range and likes range")
    void shouldFilterMoviesByCombiningAllFilters() {
        MoviesFilter filter = new MoviesFilter("inter", 2010, 2015, 50, 100);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent())
                .extracting(MovieEntity::getId)
                .containsExactly("M3");
    }

    @Test
    @DisplayName("Should return empty list when combined filters have no match")
    void shouldReturnEmptyWhenCombinedFiltersDoNotMatch() {
        MoviesFilter filter = new MoviesFilter("Matrix", 2010, 2020, 10, 50);
        MoviesSpecification specification = new MoviesSpecification(filter);

        Page<MovieEntity> result = moviesRepository.findAll(specification, Pageable.unpaged());

        assertThat(result.getContent()).isEmpty();
    }
}
