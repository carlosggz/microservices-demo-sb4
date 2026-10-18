package org.example.actorsapi.application;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.actorsapi.domain.components.ActorsStore;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final ActorsStore actorsStore;

    public Mono<Void> likeActorsInMovie(@NonNull String id) {

        Assert.isTrue(StringUtils.isNotBlank(id), "id must not be null or empty");

        return actorsStore
                .likeActorsInMovie(id);
    }
}
