package org.example.actorsapi.application;

import lombok.RequiredArgsConstructor;
import org.example.actorsapi.domain.components.ActorsStore;
import org.example.actorsapi.domain.models.ActorInfoDto;
import org.example.actorsapi.domain.models.ActorsFilter;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ListActorsService {

    private final ActorsStore actorsStore;

    public Flux<ActorInfoDto> getActorsList(@Nullable ActorsFilter filter) {
        return actorsStore.getActors(filter);
    }
}
