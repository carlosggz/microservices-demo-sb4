package org.example.moviesapi.application;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.moviesapi.domain.components.MoviesStore;
import org.example.moviesapi.domain.exceptions.UnknownMovieException;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class DeleteMoviesService {
    private final MoviesStore moviesStore;

    public void deleteMovie(@NonNull String id) throws UnknownMovieException {
        Assert.isTrue(StringUtils.isNotBlank(id), "id must not be null or empty");

        if (!moviesStore.existsMovie(id)) {
            throw new UnknownMovieException("Movie with id " + id + " not found");
        }

        moviesStore.deleteMovie(id);
    }
}
