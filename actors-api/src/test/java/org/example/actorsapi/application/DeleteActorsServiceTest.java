package org.example.actorsapi.application;

import lombok.val;
import org.example.actorsapi.domain.exceptions.UnknownActorException;
import org.example.actorsapi.infrastructure.persistence.ActorEntity;
import org.example.actorsapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class DeleteActorsServiceTest extends IntegrationTest {

    @Autowired
    private DeleteActorsService service;

    @ParameterizedTest
    @MethodSource("provideActorIds")
    void whenInvalidIdItThrowsException(String id) {
        assertThrows(IllegalArgumentException.class, () -> service.deleteActor(id));
    }

    @Test
    void whenActorDoesNotExistsItThrowsException() {
        StepVerifier
                .create(service.deleteActor("invalid"))
                .expectError(UnknownActorException.class)
                .verify();

        verify(actorsStore).existsActor("invalid");
        verifyNoMoreInteractions(actorsStore);
    }

    @Test
    void whenActorExistsItIsDeleted() {
        //given
        val id = "valid";
        actorsRepository.save(ActorEntity.builder()
                        .id(id)
                        .firstName("firstName")
                        .lastName("lastname")
                        .character("character")
                        .likes(0)
                        .movies(Set.of("m1"))
                        .build())
                .block();

        //when/then
        StepVerifier
                .create(service.deleteActor(id))
                .verifyComplete();

        verify(actorsStore).existsActor(id);
        verify(actorsStore).deleteActor(id);
        verifyNoMoreInteractions(actorsStore);

        assertEquals(0, actorsRepository.count().block());
    }

    private static Stream<String> provideActorIds() {
        return Stream.of((String) null, "", " ");
    }
}