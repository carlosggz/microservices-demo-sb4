package org.example.actorsapi;

import lombok.val;
import org.example.actorsapi.domain.models.LikeMovieEvent;
import org.example.actorsapi.infrastructure.persistence.ActorEntity;
import org.example.actorsapi.utils.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActorsApiApplicationTests extends IntegrationTest {

    @Autowired
    private Consumer<Flux<LikeMovieEvent>> likesConsumer;

    @Test
    @DisplayName("likesConsumer successfully consumes event and increments likes of actors in movie")
    void likesConsumerIncrementsLikesTest() {
        //given
        val actor = ActorEntity.builder()
                .id("actor-01")
                .firstName("Robert")
                .lastName("Downey")
                .character("Iron Man")
                .likes(10)
                .movies(Set.of("movie-marvel-1"))
                .build();
        actorsRepository.save(actor).block();

        val event = new LikeMovieEvent(
                "movie-marvel-1",
                "movie.like",
                LocalDateTime.now(),
                1
        );

        //when
        likesConsumer.accept(Flux.just(event));

        //then
        val updatedActor = actorsRepository.findById("actor-01").block();
        assertEquals(11, updatedActor.getLikes());
    }

    @Test
    void contextLoads() {
    }

}
