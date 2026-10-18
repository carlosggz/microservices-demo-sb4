package org.example.actorsapi.application;

import org.example.actorsapi.domain.exceptions.UnknownActorException;
import org.example.actorsapi.domain.models.ActorDto;
import org.example.actorsapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class UpdateActorsServiceTest extends IntegrationTest {

    private static final ActorDto ACTOR = new ActorDto(
            "a1",
            "firstName",
            "lastName",
            "character",
            0,
            Set.of("m1", "m2")
    );

    @Autowired
    private UpdateActorsService service;

    @Test
    void whenDtoIsNullItThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.updateActor(null));
    }

    @Test
    void whenActorDoesNotExistsItThrowsException() {
        StepVerifier
                .create(service.updateActor(ACTOR))
                .expectError(UnknownActorException.class)
                .verify();

        verify(actorsStore).existsActor(ACTOR.id());
        verifyNoMoreInteractions(actorsStore);
    }

    @Test
    void whenActorAlreadyExistItUpdatesActor() {
        //given
        actorsRepository
                .save(mapper.toEntity(ACTOR.withFirstName(ACTOR.firstName() + " initial")))
                .block();

        //when/then
        StepVerifier
                .create(service.updateActor(ACTOR))
                .verifyComplete();

        verify(actorsStore).existsActor(ACTOR.id());
        verify(actorsStore).saveActor(ACTOR);

        assertEquals(1, actorsRepository
                .count()
                .block());
        assertEquals(ACTOR, actorsRepository
                .findById(ACTOR.id())
                .map(mapper::toDetailsDto)
                .block());
    }
}