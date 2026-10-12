package org.example.moviesapi.application;

import lombok.SneakyThrows;
import lombok.val;
import org.example.moviesapi.domain.exceptions.UnknownMovieException;
import org.example.moviesapi.domain.models.MovieDto;
import org.example.moviesapi.infrastructure.persistence.MovieEntity;
import org.example.moviesapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class UpdateMoviesServiceTest extends IntegrationTest {

    @Autowired
    private UpdateMoviesService service;

    @Test
    void whenUpdateWithNullMovieItThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.updateMovie(null));
    }

    @Test
    void whenMovieDoesNotExistsItThrowsException() {
        assertThrows(
                UnknownMovieException.class,
                () -> service.updateMovie(new MovieDto("123", "title", 2001, "imbd", 0)));
        verify(moviesStore).existsMovie(eq("123"));
        verifyNoMoreInteractions(moviesStore);
    }

    @Test
    @SneakyThrows
    void whenMovieExistsItIsUpdated() {
        //given
        val toUpdate = new MovieDto("abc", "title", 2001, "imbd", 2);
        val existing = moviesRepository.save(MovieEntity.builder()
                .id(toUpdate.id())
                .title("old title")
                .releaseYear(2000)
                .imdb("old imbd")
                .likes(0)
                .build());

        //when
        service.updateMovie(toUpdate);

        //then
        verify(moviesStore).existsMovie(eq(toUpdate.id()));
        verify(moviesStore).saveMovie(toUpdate);
        assertEquals(1, moviesRepository.count());
        val updated = moviesRepository.findById(toUpdate.id());
        assertNotNull(updated);
        assertTrue(updated.isPresent());
        assertEquals(toUpdate.id(), updated.get().getId());
        assertEquals(toUpdate.title(), updated.get().getTitle());
        assertEquals(toUpdate.releaseYear(), updated.get().getReleaseYear());
        assertEquals(toUpdate.imdb(), updated.get().getImdb());
        assertEquals(toUpdate.likes(), updated.get().getLikes());
        assertEquals(
                existing.getCreatedAt().withNano(0),
                updated.get().getCreatedAt().withNano(0));
        assertNotNull(updated.get().getUpdatedAt());
        assertTrue(existing.getUpdatedAt().isBefore(updated.get().getUpdatedAt()));
    }
}