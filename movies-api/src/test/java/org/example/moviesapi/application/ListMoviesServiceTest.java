package org.example.moviesapi.application;

import lombok.val;
import org.example.moviesapi.domain.models.MoviesFilter;
import org.example.moviesapi.infrastructure.persistence.MovieEntity;
import org.example.moviesapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class ListMoviesServiceTest extends IntegrationTest {

    @Autowired
    private ListMoviesService service;

    @Test
    void whenGetListItAppliesTheFilter() {
        //given
        moviesRepository.save(MovieEntity.builder()
                .id("abc")
                .title("abc title")
                .releaseYear(1972)
                .imdb("def imdb")
                .build());
        moviesRepository.save(MovieEntity.builder()
                .id("def")
                .title("def title")
                .releaseYear(1982)
                .imdb("def imdb")
                .build());
        moviesRepository.save(MovieEntity.builder()
                .id("ghi")
                .title("ghi title")
                .releaseYear(1992)
                .imdb("def imdb")
                .build());

        val filter = new MoviesFilter("def", 1980, 1985, null, null);

        //when
        val result = service.getMoviesList(filter);

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("def", result.getFirst().id());

        verify(moviesStore).getMovies(filter);
        verifyNoMoreInteractions(moviesStore);
    }

}