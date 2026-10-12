package org.example.moviesapi.application;

import lombok.val;
import org.example.moviesapi.domain.models.MovieDto;
import org.example.moviesapi.infrastructure.mappers.MovieMapper;
import org.example.moviesapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class DetailsMovieServiceTest extends IntegrationTest {

    @Autowired
    private DetailsMovieService service;

    @Autowired
    private MovieMapper movieMapper;

    @ParameterizedTest
    @MethodSource("invalidIds")
    void whenInvalidIdItThrowsException(String id) {
        assertThrows(IllegalArgumentException.class, () -> service.getMovieById(id));
    }

    @Test
    void whenMovieDoesNotExistsItReturnsEmpty() {
        //given
        val id = "invalid-id";

        //when
        val result = service.getMovieById(id);

        //then
        assertTrue(result.isEmpty());
        verify(moviesStore).getMovie(id);
        verifyNoMoreInteractions(moviesStore);
    }

    @Test
    void whenMovieExistsItIsReturned() {
        //given
        val movie = new MovieDto("abc", "title", 2000, "imbd", 0);
        moviesRepository.save(movieMapper.toEntity(movie));

        //when
        val result = service.getMovieById(movie.id());

        //then
        assertTrue(result.isPresent());
        assertEquals(movie, result.get());
        verify(moviesStore).getMovie(eq(movie.id()));
        verifyNoMoreInteractions(moviesStore);
    }

    public static Stream<Arguments> invalidIds() {
        return Stream.of(
                Arguments.of((String)null),
                Arguments.of(""),
                Arguments.of(" ")
        );
    }
}