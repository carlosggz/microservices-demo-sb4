package org.example.actorsapi.infrastructure.components;

import lombok.val;
import org.example.actorsapi.domain.models.ActorDto;
import org.example.actorsapi.domain.models.ActorsFilter;
import org.example.actorsapi.infrastructure.persistence.ActorEntity;
import org.example.actorsapi.utils.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActorsStoreImplTest extends IntegrationTest {

    private static final ActorEntity ACTOR_1 = ActorEntity.builder()
            .id("actor-01")
            .firstName("Robert")
            .lastName("Downey")
            .character("Iron Man")
            .likes(100)
            .movies(Set.of("movie-marvel-1", "movie-marvel-2"))
            .build();

    private static final ActorEntity ACTOR_2 = ActorEntity.builder()
            .id("actor-02")
            .firstName("Chris")
            .lastName("Evans")
            .character("Captain America")
            .likes(80)
            .movies(Set.of("movie-marvel-1"))
            .build();

    private static final ActorEntity ACTOR_3 = ActorEntity.builder()
            .id("actor-03")
            .firstName("Robert")
            .lastName("Pattinson")
            .character("Batman")
            .likes(50)
            .movies(Set.of("movie-dc-1"))
            .build();

    private static final ActorEntity ACTOR_SPECIAL_CHARS = ActorEntity.builder()
            .id("actor-04")
            .firstName("Benedict")
            .lastName("Cumberbatch")
            .character("Dr. Strange (MCU) [v1.0] *special* + $")
            .likes(30)
            .movies(Set.of("movie-marvel-3"))
            .build();

    @Nested
    @DisplayName("Tests for getActors(ActorsFilter filter)")
    class GetActorsTests {

        @Test
        @DisplayName("When filter is null, returns all actors from repository")
        void whenFilterIsNull_returnsAllActors() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            StepVerifier
                    .create(actorsStore.getActors(null))
                    .expectNext(mapper.toInfoDto(ACTOR_1))
                    .expectNext(mapper.toInfoDto(ACTOR_2))
                    .expectNext(mapper.toInfoDto(ACTOR_3))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter is empty (all fields null), returns all actors")
        void whenFilterIsEmpty_returnsAllActors() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter(null, null, null, null);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNextCount(3)
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter text matches actor ID case-insensitively, returns matching actor")
        void whenFilterMatchesId_returnsMatchingActor() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter("ACTOR-02", null, null, null);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_2))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter text matches firstName case-insensitively, returns matching actors")
        void whenFilterMatchesFirstName_returnsMatchingActors() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter("robert", null, null, null);

            StepVerifier.create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_1))
                    .expectNext(mapper.toInfoDto(ACTOR_3))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter text matches lastName case-insensitively, returns matching actor")
        void whenFilterMatchesLastName_returnsMatchingActor() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter("EVANS", null, null, null);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_2))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter text matches character case-insensitively, returns matching actor")
        void whenFilterMatchesCharacter_returnsMatchingActor() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter("iron man", null, null, null);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_1))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter text contains leading/trailing whitespace, trims and matches actor")
        void whenFilterTextHasWhitespaces_trimsAndMatches() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter("   Downey   ", null, null, null);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_1))
                    .verifyComplete();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("When filter text is null, empty or blank, text criteria is ignored")
        void whenFilterTextIsBlank_ignoresTextCriteria(String blankText) {
            seedActors(ACTOR_1, ACTOR_2);

            val filter = new ActorsFilter(blankText, null, null, null);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNextCount(2)
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter text has regex special characters, Pattern.quote ensures literal match")
        void whenFilterTextContainsRegexSpecialCharacters_matchesLiterally() {
            seedActors(ACTOR_1, ACTOR_SPECIAL_CHARS);

            val filter = new ActorsFilter("Dr. Strange (MCU) [v1.0] *special* + $", null, null, null);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_SPECIAL_CHARS))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter by startLikes, returns actors with likes greater than or equal to startLikes")
        void whenFilterByStartLikes_returnsActorsWithLikesGte() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter(null, 80, null, null);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_1))
                    .expectNext(mapper.toInfoDto(ACTOR_2))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter by endLikes, returns actors with likes less than or equal to endLikes")
        void whenFilterByEndLikes_returnsActorsWithLikesLte() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter(null, null, 80, null);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_2))
                    .expectNext(mapper.toInfoDto(ACTOR_3))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter by startLikes and endLikes range, returns actors within the range")
        void whenFilterByLikesRange_returnsActorsWithinRange() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter(null, 60, 90, null);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_2))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter by movieId, returns actors appearing in that movie")
        void whenFilterByMovieId_returnsMatchingActors() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter(null, null, null, "movie-marvel-1");

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_1))
                    .expectNext(mapper.toInfoDto(ACTOR_2))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When filter by movieId with leading/trailing whitespaces, trims and matches actors")
        void whenFilterByMovieIdWithWhitespaces_trimsAndMatches() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter(null, null, null, "   movie-dc-1   ");

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_3))
                    .verifyComplete();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("When filter movieId is null, empty or blank, movie criteria is ignored")
        void whenFilterMovieIdIsBlank_ignoresMovieCriteria(String blankMovieId) {
            seedActors(ACTOR_1, ACTOR_2);

            val filter = new ActorsFilter(null, null, null, blankMovieId);

            StepVerifier
                    .create(actorsStore.getActors(filter))
                    .expectNextCount(2)
                    .verifyComplete();
        }

        @Test
        @DisplayName("When combining text, startLikes, endLikes and movieId, combines with AND operator")
        void whenFilterByMultipleCriteria_combinesWithAndOperator() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter("Robert", 90, 110, "movie-marvel-1");

            StepVerifier.create(actorsStore.getActors(filter))
                    .expectNext(mapper.toInfoDto(ACTOR_1))
                    .verifyComplete();
        }

        @Test
        @DisplayName("When no actors match the filter criteria, returns empty Flux")
        void whenNoActorsMatchFilter_returnsEmptyFlux() {
            seedActors(ACTOR_1, ACTOR_2, ACTOR_3);

            val filter = new ActorsFilter("Spider-Man", 500, 1000, "movie-non-existent");

            StepVerifier.create(actorsStore.getActors(filter))
                    .expectNextCount(0)
                    .verifyComplete();
        }

        @Test
        @DisplayName("When database is empty, returns empty Flux for any filter")
        void whenDatabaseIsEmpty_returnsEmptyFlux() {
            val filter = new ActorsFilter("Robert", 10, 100, "movie-1");

            StepVerifier.create(actorsStore.getActors(filter))
                    .expectNextCount(0)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Tests for other ActorsStore methods")
    class OtherMethodsTests {

        @Test
        @DisplayName("existsActor returns true when actor exists, false otherwise")
        void existsActorTest() {
            seedActors(ACTOR_1);

            StepVerifier
                    .create(actorsStore.existsActor(ACTOR_1.getId()))
                    .assertNext(Assertions::assertTrue)
                    .verifyComplete();

            StepVerifier
                    .create(actorsStore.existsActor("non-existing-id"))
                    .assertNext(Assertions::assertFalse)
                    .verifyComplete();
        }

        @Test
        @DisplayName("getActor returns details DTO when actor exists, empty Mono otherwise")
        void getActorTest() {
            seedActors(ACTOR_1);

            StepVerifier
                    .create(actorsStore.getActor(ACTOR_1.getId()))
                    .expectNext(mapper.toDetailsDto(ACTOR_1))
                    .verifyComplete();

            StepVerifier
                    .create(actorsStore.getActor("non-existing-id"))
                    .verifyComplete();
        }

        @Test
        @DisplayName("saveActor successfully stores an actor in repository")
        void saveActorTest() {
            val dto = new ActorDto("actor-new", "Tom", "Holland", "Spider-Man", 75, Set.of("movie-marvel-4"));

            StepVerifier
                    .create(actorsStore.saveActor(dto))
                    .verifyComplete();

            val saved = actorsRepository.findById("actor-new").block();
            assertEquals("actor-new", saved.getId());
            assertEquals("Tom", saved.getFirstName());
            assertEquals("Holland", saved.getLastName());
            assertEquals("Spider-Man", saved.getCharacter());
            assertEquals(75, saved.getLikes());
            assertEquals(Set.of("movie-marvel-4"), saved.getMovies());
        }

        @Test
        @DisplayName("deleteActor successfully removes an actor from repository")
        void deleteActorTest() {
            seedActors(ACTOR_1);

            StepVerifier
                    .create(actorsStore.deleteActor(ACTOR_1.getId()))
                    .verifyComplete();

            StepVerifier
                    .create(actorsRepository.existsById(ACTOR_1.getId()))
                    .assertNext(Assertions::assertFalse)
                    .verifyComplete();
        }

        @Test
        @DisplayName("likeActorsInMovie successfully increments likes for all actors in the movie")
        void likeActorsInMovieTest() {
            seedActors(ACTOR_1, ACTOR_2);

            StepVerifier
                    .create(actorsStore.likeActorsInMovie("movie-marvel-1"))
                    .verifyComplete();

            val actor1 = actorsRepository.findById(ACTOR_1.getId()).block();
            val actor2 = actorsRepository.findById(ACTOR_2.getId()).block();

            assertEquals(ACTOR_1.getLikes() + 1, actor1.getLikes());
            assertEquals(ACTOR_2.getLikes() + 1, actor2.getLikes());
        }

        @Test
        @DisplayName("likeActorsInMovie only increments likes for actors in the specific movie")
        void likeActorsInMovieSpecificMovieTest() {
            seedActors(ACTOR_1, ACTOR_2);

            StepVerifier
                    .create(actorsStore.likeActorsInMovie("movie-marvel-2"))
                    .verifyComplete();

            val actor1 = actorsRepository.findById(ACTOR_1.getId()).block();
            val actor2 = actorsRepository.findById(ACTOR_2.getId()).block();

            assertEquals(ACTOR_1.getLikes() + 1, actor1.getLikes());
            assertEquals(ACTOR_2.getLikes(), actor2.getLikes());
        }
    }

    private void seedActors(ActorEntity... entities) {
        actorsRepository.saveAll(List.of(entities)).collectList().block();
    }
}
