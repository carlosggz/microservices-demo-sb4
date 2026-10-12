package org.example.moviesapi.application;

import lombok.SneakyThrows;
import lombok.val;
import org.awaitility.Awaitility;
import org.example.moviesapi.domain.components.EventsPublisher;
import org.example.moviesapi.domain.exceptions.UnknownMovieException;
import org.example.moviesapi.domain.models.LikeMovieEvent;
import org.example.moviesapi.domain.models.MovieDto;
import org.example.moviesapi.infrastructure.mappers.MovieMapper;
import org.example.moviesapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@EnableTestBinder
class LikeMoviesServiceTest extends IntegrationTest {

    @Autowired
    private LikeMoviesService service;

    @Autowired
    private MovieMapper movieMapper;

    @MockitoSpyBean
    private EventsPublisher eventsPublisher;

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @MethodSource("invalidIds")
    void whenInvalidIdItThrowsException(String id) {
        assertThrows(IllegalArgumentException.class, () -> service.likeMovie(id));
    }

    @Test
    void whenMovieDoesNotExistsItReturnsEmpty() {
        //given
        val id = "invalid-id";

        //when/then
        assertThrows(UnknownMovieException.class, () -> service.likeMovie(id));

        //then
        verify(moviesStore).getMovie(id);
        verifyNoMoreInteractions(moviesStore);
    }

    @Test
    @SneakyThrows
    void whenMovieExistsItIsLiked() {
        //given
        val startDate = LocalDateTime.now();
        val movie = new MovieDto("abc", "title", 2000, "imbd", 3);
        val added = moviesRepository.save(movieMapper.toEntity(movie));

        //when
        service.likeMovie(movie.id());

        //then
        verify(moviesStore).getMovie(eq(movie.id()));
        verify(moviesStore).saveMovie(any());
        verify(eventsPublisher).publish(any());
        verifyNoMoreInteractions(moviesStore);

        assertEquals(1, moviesRepository.count());
        val likedMovie = moviesRepository.findById(movie.id());
        assertNotNull(likedMovie);
        assertTrue(likedMovie.isPresent());
        assertEquals(movie.id(), likedMovie.get().getId());
        assertEquals(movie.title(), likedMovie.get().getTitle());
        assertEquals(movie.releaseYear(), likedMovie.get().getReleaseYear());
        assertEquals(movie.imdb(), likedMovie.get().getImdb());
        assertEquals(movie.likes() + 1, likedMovie.get().getLikes());
        assertEquals(added.getCreatedAt().withNano(0), likedMovie.get().getCreatedAt().withNano(0));
        assertNotNull(likedMovie.get().getUpdatedAt());
        assertTrue(added.getUpdatedAt().isBefore(likedMovie.get().getUpdatedAt()));

        Awaitility
                .waitAtMost(Duration.ofSeconds(5))
                .await()
                .untilAsserted(() -> {
                    val message = outputDestination.receive(1000, "movies-topic");
                    assertNotNull(message);

                    val movieEvent = objectMapper.readValue(message.getPayload(), LikeMovieEvent.class);
                    assertNotNull(movieEvent);
                    assertEquals(movie.id(), movieEvent.getMovieId());
                    assertEquals(movie.likes()+1, movieEvent.getLikes());
                    assertNotNull(movieEvent.getOccurrenceDate());
                    assertTrue(startDate.isBefore(movieEvent.getOccurrenceDate()));
                    assertEquals(LikeMovieEvent.EVENT_NAME, movieEvent.getEventName());
                });
    }

    public static Stream<Arguments> invalidIds() {
        return Stream.of(
                Arguments.of((String)null),
                Arguments.of(""),
                Arguments.of(" ")
        );
    }
}