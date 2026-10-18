package org.example.actorsapi.domain.models;

import org.jspecify.annotations.Nullable;

public record ActorsFilter(
        @Nullable String text,
        @Nullable Integer startLikes,
        @Nullable Integer endLikes,
        @Nullable String movieId) {
}
