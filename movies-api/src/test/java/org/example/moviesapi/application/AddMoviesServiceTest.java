package org.example.moviesapi.application;

import lombok.SneakyThrows;
import lombok.val;
import org.example.moviesapi.domain.exceptions.MovieAlreadyExistsException;
import org.example.moviesapi.domain.models.MovieDto;
import org.example.moviesapi.infrastructure.persistence.MovieEntity;
import org.example.moviesapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AddMoviesServiceTest extends IntegrationTest {

    @Autowired
    private AddMoviesService service;

    @Test
    void whenAddWithNullMovieItThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.addMovie(null));
    }

    @Test
    void whenMovieAlreadyExistsItThrowsException() {
        //given
        val movie = new MovieDto("abc", "title", 2001, "imbd", 0);
        moviesRepository.save(MovieEntity.builder()
                .id("abc")
                .title("some title")
                .releaseYear(2000)
                .imdb("imdb")
                .build());

        //when/then
        assertThrows(MovieAlreadyExistsException.class, () -> service.addMovie(movie));

        verify(moviesStore).existsMovie(eq(movie.id()));
        verifyNoMoreInteractions(moviesStore);
    }

    @Test
    @SneakyThrows
    void whenMovieDoesNotExistsItIsSaved() {
        //given
        val toAdd = new MovieDto("abc", "title", 2001, "imbd", 2);

        //when
        service.addMovie(toAdd);

        //then
        verify(moviesStore).existsMovie(eq(toAdd.id()));
        verify(moviesStore).saveMovie(toAdd);
        assertEquals(1, moviesRepository.count());
        val added = moviesRepository.findById(toAdd.id());
        assertNotNull(added);
        assertTrue(added.isPresent());
        assertEquals(toAdd.id(), added.get().getId());
        assertEquals(toAdd.title(), added.get().getTitle());
        assertEquals(toAdd.releaseYear(), added.get().getReleaseYear());
        assertEquals(toAdd.imdb(), added.get().getImdb());
        assertEquals(toAdd.likes(), added.get().getLikes());
        assertNotNull(added.get().getCreatedAt());
        assertNotNull(added.get().getUpdatedAt());
    }
}