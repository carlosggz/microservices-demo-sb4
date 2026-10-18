package org.example.actorsapi.infrastructure.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.example.actorsapi.domain.components.ActorsStore;
import org.example.actorsapi.domain.models.ActorDto;
import org.example.actorsapi.domain.models.ActorInfoDto;
import org.example.actorsapi.domain.models.ActorsFilter;
import org.example.actorsapi.infrastructure.mappers.ActorMapper;
import org.example.actorsapi.infrastructure.persistence.ActorEntity;
import org.example.actorsapi.infrastructure.persistence.ActorsRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActorsStoreImpl implements ActorsStore {

    private final ActorsRepository repository;
    private final ActorMapper mapper;
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Boolean> existsActor(String id) {
        return repository.existsById(id);
    }

    @Override
    public Flux<ActorInfoDto> getActors(ActorsFilter filter) {
        val flux = Objects.isNull(filter)
                ? repository.findAll()
                : mongoTemplate.find(getQuery(filter), ActorEntity.class);

        return flux.map(mapper::toInfoDto);
    }

    @Override
    public Mono<ActorDto> getActor(String id) {
        return repository
                .findById(id)
                .map(mapper::toDetailsDto);
    }

    @Override
    public Mono<Void> saveActor(ActorDto actorDto) {
        return repository
                .save(mapper.toEntity(actorDto))
                .then();
    }

    @Override
    public Mono<Void> deleteActor(String id) {
        return repository
                .deleteById(id);
    }

    @Override
    public Mono<Void> likeActorsInMovie(String movieId) {
        return Mono
                .justOrEmpty(getMoviesCriteria(movieId))
                .map(Query::new)
                .flatMap(query -> mongoTemplate
                        .updateMulti(
                                query,
                                new Update().inc("likes", 1),
                                ActorEntity.class))
                .doOnNext(result -> log.info("Updated {} actors", result.getModifiedCount()))
                .then();
    }

    private Query getQuery(ActorsFilter filter) {
        val criteriaList = Stream
                .of(
                        getTextCriteria(filter.text()),
                        getStartLikesCriteria(filter.startLikes()),
                        getEndLikesCriteria(filter.endLikes()),
                        getMoviesCriteria(filter.movieId())
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        val query = new Query();

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList));
        }

        return query;
    }

    private Optional<Criteria> getTextCriteria(String text) {
        return Optional
                .ofNullable(text)
                .filter(StringUtils::isNotBlank)
                .map(x -> Pattern.quote(x.trim()))
                .map(regex -> new Criteria()
                        .orOperator(
                                Criteria.where("id").regex(regex, "i"),
                                Criteria.where("firstName").regex(regex, "i"),
                                Criteria.where("lastName").regex(regex, "i"),
                                Criteria.where("character").regex(regex, "i")
                        ));
    }

    private Optional<Criteria> getStartLikesCriteria(Integer startLikes) {
        return Optional
                .ofNullable(startLikes)
                .map(x -> Criteria.where("likes").gte(x));
    }

    private Optional<Criteria> getEndLikesCriteria(Integer endLikes) {
        return Optional
                .ofNullable(endLikes)
                .map(x -> Criteria.where("likes").lte(x));
    }

    private Optional<Criteria> getMoviesCriteria(String movieId) {
        return Optional
                .ofNullable(movieId)
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .map(x -> Criteria.where("movies").in(x));
    }
}
