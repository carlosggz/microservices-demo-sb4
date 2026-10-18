package org.example.actorsapi.application;

import org.example.actorsapi.domain.exceptions.ActorAlreadyExistsException;
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

class AddActorsServiceTest extends IntegrationTest {

    private static final ActorDto ACTOR = new ActorDto(
            "a1",
            "firstName",
            "lastName",
            "character",
            0,
            Set.of("m1", "m2")
    );

    @Autowired
    private AddActorsService service;

    @Test
    void whenDtoIsNullItThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.addActor(null));
    }

    @Test
    void whenActorAlreadyExistsItThrowsException() {
        //given
        actorsRepository
                .save(mapper.toEntity(ACTOR.withFirstName(ACTOR.firstName() + " initial")))
                .block();

        //when/then
        StepVerifier
                .create(service.addActor(ACTOR))
                .expectError(ActorAlreadyExistsException.class)
                .verify();

        verify(actorsStore).existsActor(ACTOR.id());
        verifyNoMoreInteractions(actorsStore);

        assertEquals(1, actorsRepository
                .count()
                .block());
    }

    @Test
    void whenActorDoesNotExistItSavesActor() {
        StepVerifier
                .create(service.addActor(ACTOR))
                .verifyComplete();

        verify(actorsStore).existsActor(ACTOR.id());
        verify(actorsStore).saveActor(ACTOR);
        verifyNoMoreInteractions(actorsStore);

        assertEquals(1, actorsRepository
                .count()
                .block());
        assertEquals(ACTOR, actorsRepository
                .findById(ACTOR.id())
                .map(mapper::toDetailsDto)
                .block());
    }
}