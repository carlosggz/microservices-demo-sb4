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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class DetailsActorServiceTest extends IntegrationTest  {

    @Autowired
    private DetailsActorService service;

    @ParameterizedTest
    @MethodSource("provideActorIds")
    void whenInvalidIdItThrowsException(String id) {
        assertThrows(IllegalArgumentException.class, () -> service.getActorById(id));
    }

    @Test
    void whenActorDoesNotExistsItReturnsEmpty() {
        StepVerifier
                .create(service.getActorById("invalid"))
                .expectError(UnknownActorException.class)
                .verify();

        verify(actorsStore).getActor("invalid");
        verifyNoMoreInteractions(actorsStore);
    }

    @Test
    void whenActorExistsItIsReturned() {
        //given
        val id = "valid";
        val entity = actorsRepository.save(ActorEntity.builder()
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
                .create(service.getActorById(id))
                .expectNext(mapper.toDetailsDto(entity))
                .verifyComplete();

        verify(actorsStore).getActor(id);
        verifyNoMoreInteractions(actorsStore);
    }

    private static Stream<String> provideActorIds() {
        return Stream.of((String) null, "", " ");
    }
}