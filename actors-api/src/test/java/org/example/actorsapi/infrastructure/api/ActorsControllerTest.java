package org.example.actorsapi.infrastructure.api;

import lombok.val;
import org.example.actorsapi.application.*;
import org.example.actorsapi.domain.exceptions.ActorAlreadyExistsException;
import org.example.actorsapi.domain.exceptions.UnknownActorException;
import org.example.actorsapi.domain.models.ActorDto;
import org.example.actorsapi.domain.models.ActorInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@WebFluxTest(ActorsController.class)
class ActorsControllerTest {

    private static final ActorDto ACTOR = new ActorDto("a1", "firstname", "lastname", "character", 0, Set.of("m1"));

    @MockitoBean
    private AddActorsService addActorService;

    @MockitoBean
    private DeleteActorsService deleteActorService;

    @MockitoBean
    private UpdateActorsService updateActorsService;

    @MockitoBean
    private DetailsActorService detailsActorService;

    @MockitoBean
    private ListActorsService listActorsService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getActorsCallsService() {
        //given
        val actor1 = new ActorInfoDto("a1", "firstname1", "lastname1");
        val actor2 = new ActorInfoDto("a2", "firstname2", "lastname2");
        doReturn(Flux.just(actor1, actor2))
                .when(listActorsService)
                .getActorsList(any());

        //when
        webTestClient
                .get()
                .uri("/api/v1/actors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ActorInfoDto.class)
                .isEqualTo(List.of(actor1, actor2));
    }

    @Test
    void whenGetDetailsOfInvalidActorItReturnsNotFound() {
        //given
        val id = "invalid";
        doReturn(Mono.error(new UnknownActorException()))
                .when(detailsActorService)
                .getActorById(eq(id));

        //when/then
        webTestClient
                .get()
                .uri("/api/v1/actors/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenGetDetailsThrowsErrorItReturnsInternalServerError() {
        //given
        val id = "invalid";
        doReturn(Mono.error(new RuntimeException()))
                .when(detailsActorService)
                .getActorById(eq(id));

        //when/then
        webTestClient
                .get()
                .uri("/api/v1/actors/{id}", id)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void whenGetDetailsOfValidActorItReturnsTheActorDetails() {
        //given
        doReturn(Mono.just(ACTOR))
                .when(detailsActorService)
                .getActorById(eq(ACTOR.id()));

        //when/then
        webTestClient
                .get()
                .uri("/api/v1/actors/{id}", ACTOR.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ActorDto.class)
                .isEqualTo(ACTOR);
    }

    @Test
    void whenAddAnExistentActorItReturnsBadRequest() {
        //given
        doReturn(Mono.error(new ActorAlreadyExistsException()))
                .when(addActorService)
                .addActor(ACTOR);

        //when/then
        webTestClient
                .post()
                .uri("/api/v1/actors")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenAddAnAndGetAnErrorItReturnsInternalServerError() {
        //given
        doReturn(Mono.error(new RuntimeException()))
                .when(addActorService)
                .addActor(ACTOR);

        //when/then
        webTestClient
                .post()
                .uri("/api/v1/actors")
                .bodyValue(ACTOR)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void whenAddAnActorItCallsService() {
        //given
        doReturn(Mono.empty())
                .when(addActorService)
                .addActor(ACTOR);

        //when/then
        webTestClient
                .post()
                .uri("/api/v1/actors")
                .bodyValue(ACTOR)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void whenUpdateAnInvalidActorItReturnsNotFound() {
        //given
        doReturn(Mono.error(new UnknownActorException()))
                .when(updateActorsService)
                .updateActor(ACTOR);

        //when/then
        webTestClient
                .put()
                .uri("/api/v1/actors")
                .bodyValue(ACTOR)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenUpdateAnAndGetAnErrorItReturnsInternalServerError() {
        //given
        doReturn(Mono.error(new RuntimeException()))
                .when(updateActorsService)
                .updateActor(ACTOR);

        //when/then
        webTestClient
                .put()
                .uri("/api/v1/actors")
                .bodyValue(ACTOR)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void whenUpdateAnActorItCallsService() {
        //given
        doReturn(Mono.empty())
                .when(updateActorsService)
                .updateActor(ACTOR);

        //when/then
        webTestClient
                .put()
                .uri("/api/v1/actors")
                .bodyValue(ACTOR)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void whenDeleteAnInvalidActorItReturnsNotFound() {
        //given
        doReturn(Mono.error(new UnknownActorException()))
                .when(deleteActorService)
                .deleteActor(eq(ACTOR.id()));

        //when/then
        webTestClient
                .delete()
                .uri("/api/v1/actors/{id}", ACTOR.id())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenDeleteAnAndGetAnErrorItReturnsInternalServerError() {
        //given
        doReturn(Mono.error(new RuntimeException()))
                .when(deleteActorService)
                .deleteActor(eq(ACTOR.id()));

        //when/then
        webTestClient
                .delete()
                .uri("/api/v1/actors/{}", ACTOR.id())
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void whenDeleteAnActorItCallsService() {
        //given
        doReturn(Mono.empty())
                .when(deleteActorService)
                .deleteActor(eq(ACTOR.id()));

        //when/then
        webTestClient
                .delete()
                .uri("/api/v1/actors/{id}", ACTOR.id())
                .exchange()
                .expectStatus().isOk();
    }
}