package org.example.moviesapi.application;

import lombok.SneakyThrows;
import lombok.val;
import org.example.moviesapi.domain.exceptions.UnknownMovieException;
import org.example.moviesapi.infrastructure.persistence.MovieEntity;
import org.example.moviesapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DeleteMoviesServiceTest extends IntegrationTest {

    @Autowired
    private DeleteMoviesService service;

    @ParameterizedTest
    @MethodSource("invalidIds")
    void whenInvalidIdItThrowsException(String id) {
        assertThrows(IllegalArgumentException.class, () -> service.deleteMovie(id));
    }

    @Test
    void whenMoviesDoesNotExistsItThrowsException() {
        //given
        val id = "invalid-id";

        //when/then
        assertThrows(UnknownMovieException.class, () -> service.deleteMovie(id));

        verify(moviesStore).existsMovie(id);
        verifyNoMoreInteractions(moviesStore);
    }

    @Test
    @SneakyThrows
    void whenMoviesExistsItDeletesIt() {
        //given
        val id = "abc";
        moviesRepository.save(MovieEntity.builder()
                        .id(id)
                        .title("title")
                        .releaseYear(2000)
                        .imdb("imdb")
                .build());

        //when
        service.deleteMovie(id);

        //then
        verify(moviesStore).existsMovie(eq(id));
        verify(moviesStore).deleteMovie(eq(id));
        assertTrue(moviesRepository.findById(id).isEmpty());
    }


    public static Stream<Arguments> invalidIds() {
        return Stream.of(
                Arguments.of((String)null),
                Arguments.of(""),
                Arguments.of(" ")
        );
    }
}