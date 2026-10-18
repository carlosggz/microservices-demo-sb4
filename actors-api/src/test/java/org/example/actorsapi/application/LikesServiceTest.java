package org.example.actorsapi.application;

import lombok.val;
import org.example.actorsapi.domain.models.ActorDto;
import org.example.actorsapi.infrastructure.persistence.ActorEntity;
import org.example.actorsapi.utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class LikesServiceTest extends IntegrationTest {

    @Autowired
    private LikesService service;

    @ParameterizedTest
    @MethodSource("provideActorIds")
    void whenInvalidIdItThrowsException(String id) {
        assertThrows(IllegalArgumentException.class, () -> service.likeActorsInMovie(id));
    }

    @Test
    void whenActorExistsItIsUpdated() {
        //given
        val movieId = "valid";
        val entity = actorsRepository.save(ActorEntity.builder()
                        .id("id")
                        .firstName("firstName")
                        .lastName("lastname")
                        .character("character")
                        .likes(0)
                        .movies(Set.of(movieId))
                        .build())
                .block();
        val expectedActor = mapper.toDetailsDto(entity).withLikes(1);

        //when/then
        StepVerifier
                .create(service.likeActorsInMovie(movieId))
                .verifyComplete();

        verify(actorsStore).likeActorsInMovie(movieId);
        verifyNoMoreInteractions(actorsStore);

        assertEquals(1, actorsRepository.count().block());
        assertEquals(expectedActor, mapper.toDetailsDto(actorsRepository.findById("id").block()));
    }

    private static Stream<String> provideActorIds() {
        return Stream.of((String) null, "", " ");
    }
}