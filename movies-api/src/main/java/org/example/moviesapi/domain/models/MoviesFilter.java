package org.example.moviesapi.domain.models;

import org.jspecify.annotations.Nullable;

public record MoviesFilter(
        @Nullable String text,
        @Nullable Integer startYear,
        @Nullable Integer endYear,
        @Nullable Integer startLikes,
        @Nullable Integer endLikes) {
}
