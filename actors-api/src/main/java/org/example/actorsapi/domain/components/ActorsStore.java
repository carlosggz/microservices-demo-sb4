package org.example.actorsapi.domain.components;

import org.example.actorsapi.domain.models.ActorDto;
import org.example.actorsapi.domain.models.ActorInfoDto;
import org.example.actorsapi.domain.models.ActorsFilter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActorsStore {
    Mono<Boolean> existsActor(String id);
    Flux<ActorInfoDto> getActors(ActorsFilter filter);
    Mono<ActorDto> getActor(String id);
    Mono<Void> saveActor(ActorDto actorDto);
    Mono<Void> deleteActor(String id);
    Mono<Void> likeActorsInMovie(String movieId);
}
