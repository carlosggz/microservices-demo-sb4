package org.example.moviesapi.domain.models;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovieDtoTest {

    private static final String VALID_ID = "movie-123";
    private static final String VALID_TITLE = "Inception";
    private static final int VALID_RELEASE_YEAR = 2010;
    private static final String VALID_IMDB = "tt1375666";
    private static final int VALID_LIKES = 100;

    @Test
    @DisplayName("Should create MovieDto successfully when all fields are valid")
    void shouldCreateMovieDtoWhenAllFieldsAreValid() {
        val movie = assertDoesNotThrow(() ->
                new MovieDto(VALID_ID, VALID_TITLE, VALID_RELEASE_YEAR, VALID_IMDB, VALID_LIKES)
        );

        assertEquals(VALID_ID, movie.id());
        assertEquals(VALID_TITLE, movie.title());
        assertEquals(VALID_RELEASE_YEAR, movie.releaseYear());
        assertEquals(VALID_IMDB, movie.imdb());
        assertEquals(VALID_LIKES, movie.likes());
    }

    @Test
    @DisplayName("Should create MovieDto when releaseYear is at the lower boundary (1960)")
    void shouldCreateMovieDtoWhenReleaseYearIsLowerBoundary() {
        val movie = assertDoesNotThrow(() ->
                new MovieDto(VALID_ID, VALID_TITLE, 1960, VALID_IMDB, VALID_LIKES)
        );

        assertEquals(1960, movie.releaseYear());
    }

    @Test
    @DisplayName("Should create MovieDto when releaseYear is at the upper boundary (current year)")
    void shouldCreateMovieDtoWhenReleaseYearIsCurrentYear() {
        val currentYear = LocalDate.now().getYear();
        val movie = assertDoesNotThrow(() ->
                new MovieDto(VALID_ID, VALID_TITLE, currentYear, VALID_IMDB, VALID_LIKES)
        );

        assertEquals(currentYear, movie.releaseYear());
    }

    @Test
    @DisplayName("Should create MovieDto when likes is zero (lower boundary)")
    void shouldCreateMovieDtoWhenLikesIsZero() {
        val movie = assertDoesNotThrow(() ->
                new MovieDto(VALID_ID, VALID_TITLE, VALID_RELEASE_YEAR, VALID_IMDB, 0)
        );

        assertEquals(0, movie.likes());
    }

    @ParameterizedTest(name = "id: \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Should throw IllegalArgumentException when id is null, empty, or blank")
    void shouldThrowExceptionWhenIdIsBlank(String invalidId) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MovieDto(invalidId, VALID_TITLE, VALID_RELEASE_YEAR, VALID_IMDB, VALID_LIKES)
        );

        assertEquals("Id cannot be empty", exception.getMessage());
    }

    @ParameterizedTest(name = "title: \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Should throw IllegalArgumentException when title is null, empty, or blank")
    void shouldThrowExceptionWhenTitleIsBlank(String invalidTitle) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MovieDto(VALID_ID, invalidTitle, VALID_RELEASE_YEAR, VALID_IMDB, VALID_LIKES)
        );

        assertEquals("Title cannot be empty", exception.getMessage());
    }

    @ParameterizedTest(name = "releaseYear: {0}")
    @ValueSource(ints = {1959, 1900, 0, -1, Integer.MIN_VALUE})
    @DisplayName("Should throw IllegalArgumentException when releaseYear is before 1960")
    void shouldThrowExceptionWhenReleaseYearIsBefore1960(int invalidYear) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MovieDto(VALID_ID, VALID_TITLE, invalidYear, VALID_IMDB, VALID_LIKES)
        );

        assertEquals("Year must be between 1960 and current year", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when releaseYear is in the future")
    void shouldThrowExceptionWhenReleaseYearIsInTheFuture() {
        int futureYear = LocalDate.now().getYear() + 1;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MovieDto(VALID_ID, VALID_TITLE, futureYear, VALID_IMDB, VALID_LIKES)
        );

        assertEquals("Year must be between 1960 and current year", exception.getMessage());
    }

    @ParameterizedTest(name = "future releaseYear: {0}")
    @ValueSource(ints = {2099, 3000, Integer.MAX_VALUE})
    @DisplayName("Should throw IllegalArgumentException when releaseYear is far in the future")
    void shouldThrowExceptionWhenReleaseYearIsFarInTheFuture(int futureYear) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MovieDto(VALID_ID, VALID_TITLE, futureYear, VALID_IMDB, VALID_LIKES)
        );

        assertEquals("Year must be between 1960 and current year", exception.getMessage());
    }

    @ParameterizedTest(name = "imdb: \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Should throw IllegalArgumentException when imdb is null, empty, or blank")
    void shouldThrowExceptionWhenImdbIsBlank(String invalidImdb) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MovieDto(VALID_ID, VALID_TITLE, VALID_RELEASE_YEAR, invalidImdb, VALID_LIKES)
        );

        assertEquals("Imdb cannot be empty", exception.getMessage());
    }

    @ParameterizedTest(name = "likes: {0}")
    @ValueSource(ints = {-1, -10, -100, Integer.MIN_VALUE})
    @DisplayName("Should throw IllegalArgumentException when likes is negative")
    void shouldThrowExceptionWhenLikesIsNegative(int invalidLikes) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new MovieDto(VALID_ID, VALID_TITLE, VALID_RELEASE_YEAR, VALID_IMDB, invalidLikes)
        );

        assertEquals("Likes cannot be negative", exception.getMessage());
    }
}
