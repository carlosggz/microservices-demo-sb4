package org.example.moviesapi.application;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.example.moviesapi.domain.components.EventsPublisher;
import org.example.moviesapi.domain.components.MoviesStore;
import org.example.moviesapi.domain.exceptions.UnknownMovieException;
import org.example.moviesapi.domain.models.LikeMovieEvent;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class LikeMoviesService {
    private final MoviesStore moviesStore;
    private final EventsPublisher eventsPublisher;

    public void likeMovie(@NonNull String id) throws UnknownMovieException {
        Assert.isTrue(StringUtils.isNotBlank(id), "id must not be blank");

        val movie = moviesStore
                .getMovie(id)
                .orElseThrow(() -> new UnknownMovieException("Movie with id " + id + " not found"));

        val modifiedMovie = movie.withLikes(movie.likes() + 1);

        moviesStore.saveMovie(modifiedMovie);
        eventsPublisher.publish(new LikeMovieEvent(modifiedMovie));
    }
}
