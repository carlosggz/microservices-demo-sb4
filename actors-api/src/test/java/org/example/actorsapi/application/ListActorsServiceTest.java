package org.example.actorsapi.application;

import lombok.val;
import org.example.actorsapi.domain.models.ActorsFilter;
import org.example.actorsapi.infrastructure.persistence.ActorEntity;
import org.example.actorsapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class ListActorsServiceTest extends IntegrationTest {

    private static final ActorEntity ACTOR1 = ActorEntity.builder()
            .id("a1")
            .firstName("First1")
            .lastName("Last1")
            .character("Char1")
            .likes(10)
            .movies(Set.of("m1"))
            .build();
    private static final ActorEntity ACTOR2 = ActorEntity.builder()
            .id("a2")
            .firstName("First2")
            .lastName("Last2")
            .character("Char2")
            .likes(11)
            .movies(Set.of("m2"))
            .build();

    @Autowired
    private ListActorsService service;

    @Test
    void whenFilterProvidedItCallsStoreWithFilter() {
        //given
        actorsRepository.save(ACTOR1).block();
        val filter = new ActorsFilter("First1", null, null, null);

        //when/then
        StepVerifier
                .create(service.getActorsList(filter))
                .assertNext(dto -> assertEquals(mapper.toInfoDto(ACTOR1), dto))
                .verifyComplete();

        verify(actorsStore).getActors(filter);
        verifyNoMoreInteractions(actorsStore);
    }

    @Test
    void whenFilterIsNullItCallsStoreWithNull() {
        //given
        actorsRepository.save(ACTOR1).block();
        actorsRepository.save(ACTOR2).block();

        //when/then
        StepVerifier
                .create(service.getActorsList(null))
                .expectNext(mapper.toInfoDto(ACTOR1))
                .expectNext(mapper.toInfoDto(ACTOR2))
                .verifyComplete();

        verify(actorsStore).getActors(null);
        verifyNoMoreInteractions(actorsStore);
    }
}