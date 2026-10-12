package org.example.moviesapi.infrastructure.api;

import lombok.SneakyThrows;
import lombok.val;
import org.example.moviesapi.application.*;
import org.example.moviesapi.domain.exceptions.MovieAlreadyExistsException;
import org.example.moviesapi.domain.exceptions.UnknownMovieException;
import org.example.moviesapi.domain.models.MovieDto;
import org.example.moviesapi.domain.models.MovieInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoviesController.class)
class MoviesControllerTest {

    private static final MovieDto MOVIE_DTO = new MovieDto("id", "title", 2000, "imdb", 0);

    @MockitoBean
    private AddMoviesService addMoviesService;

    @MockitoBean
    private DeleteMoviesService deleteMoviesService;

    @MockitoBean
    private DetailsMovieService movieDetailService;

    @MockitoBean
    private ListMoviesService moviesListService;

    @MockitoBean
    private UpdateMoviesService updateMoviesService;

    @MockitoBean
    private LikeMoviesService likeMoviesService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void getMoviesCallsMoviesListService() {
        //given
        when(moviesListService.getMoviesList(any()))
                .thenReturn(List.of(new MovieInfoDto("1", "Movie 1")));

        //when/then
        mockMvc
                .perform(get("/api/v1/movies?text=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("Movie 1"));
    }

    @Test
    @SneakyThrows
    void whenGetDetailsWithUnknownIdReturnsNotFound() {
        //given
        when(movieDetailService.getMovieById("unknown")).thenReturn(Optional.empty());

        //when/then
        mockMvc
                .perform(get("/api/v1/movies/{id}", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void whenGetDetailsWithValidIdReturnsMovieDetails() {
        //given
        val id = MOVIE_DTO.id();
        when(movieDetailService.getMovieById(id))
                .thenReturn(Optional.of(MOVIE_DTO));

        //when/then
        mockMvc
                .perform(get("/api/v1/movies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.releaseYear").value(2000))
                .andExpect(jsonPath("$.imdb").value("imdb"))
                .andExpect(jsonPath("$.likes").value(0));
    }

    @Test
    @SneakyThrows
    void whenAddMovieWithExistingMovieReturnsBadRequest() {
        //given
        doThrow(new MovieAlreadyExistsException("Movie already exists"))
                .when(addMoviesService)
                .addMovie(any());

        //when/then
        mockMvc
                .perform(post("/api/v1/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(MOVIE_DTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void whenAddMovieAndNotExpectedErrorWithExistingIdReturnsInternalError() {
        //given
        doThrow(new RuntimeException("Some error"))
                .when(addMoviesService)
                .addMovie(any());

        //when/then
        mockMvc
                .perform(post("/api/v1/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(MOVIE_DTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void whenAddMovieItReturnsCreated() {
        //given
        doNothing().when(addMoviesService).addMovie(any());

        //when/then
        mockMvc
                .perform(post("/api/v1/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(MOVIE_DTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void whenUpdateMovieWithNotExistingMovieReturnsNotFound() {
        //given
        doThrow(new UnknownMovieException("Movie not found"))
                .when(updateMoviesService)
                .updateMovie(any());

        //when/then
        mockMvc
                .perform(put("/api/v1/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(MOVIE_DTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void whenUpdateMovieAndNotExpectedErrorWithExistingIdReturnsInternalError() {
        //given
        doThrow(new RuntimeException("Some error"))
                .when(updateMoviesService)
                .updateMovie(any());

        //when/then
        mockMvc
                .perform(put("/api/v1/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(MOVIE_DTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void whenUpdateMovieItReturnsOk() {
        //given
        doNothing().when(updateMoviesService).updateMovie(any());

        //when/then
        mockMvc
                .perform(put("/api/v1/movies")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(MOVIE_DTO)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void whenDeleteMovieWithNotExistingMovieReturnsNotFound() {
        //given
        doThrow(new UnknownMovieException("Movie not found"))
                .when(deleteMoviesService)
                .deleteMovie(any());

        //when/then
        mockMvc
                .perform(delete("/api/v1/movies/{id}", MOVIE_DTO.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void whenDeleteMovieAndNotExpectedErrorWithExistingIdReturnsInternalError() {
        //given
        doThrow(new RuntimeException("Some error"))
                .when(deleteMoviesService)
                .deleteMovie(any());

        //when/then
        mockMvc
                .perform(delete("/api/v1/movies/{id}", MOVIE_DTO.id()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void whenDeleteMovieItReturnsOk() {
        //given
        doNothing().when(deleteMoviesService).deleteMovie(any());

        //when/then
        mockMvc
                .perform(delete("/api/v1/movies/{id}", MOVIE_DTO.id()))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void whenLikeMovieWithNotExistingMovieReturnsNotFound() {
        //given
        doThrow(new UnknownMovieException("Movie not found"))
                .when(likeMoviesService)
                .likeMovie(any());

        //when/then
        mockMvc
                .perform(post("/api/v1/movies/like/{id}", MOVIE_DTO.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void whenLikeMovieAndNotExpectedErrorWithExistingIdReturnsInternalError() {
        //given
        doThrow(new RuntimeException("Some error"))
                .when(likeMoviesService)
                .likeMovie(any());

        //when/then
        mockMvc
                .perform(post("/api/v1/movies/like/{id}", MOVIE_DTO.id()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void whenLikeMovieItReturnsOk() {
        //given
        doNothing().when(likeMoviesService).likeMovie(any());

        //when/then
        mockMvc
                .perform(post("/api/v1/movies/like/{id}", MOVIE_DTO.id()))
                .andExpect(status().isOk());
    }
}