package org.example.actorsapi.application;

import lombok.RequiredArgsConstructor;
import org.example.actorsapi.domain.components.ActorsStore;
import org.example.actorsapi.domain.exceptions.ActorAlreadyExistsException;
import org.example.actorsapi.domain.models.ActorDto;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddActorsService {
    private final ActorsStore actorsStore;

    public Mono<Void> addActor(@NonNull ActorDto actorDto) {
        Assert.notNull(actorDto, "actorDto must not be null");

        return actorsStore
                .existsActor(actorDto.id())
                .flatMap(exists -> exists
                        ? Mono.error(new ActorAlreadyExistsException("Actor with id " + actorDto.id() + " already exists"))
                        : actorsStore.saveActor(actorDto));
    }
}
