package org.example.actorsapi.infrastructure.persistence;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ActorsRepository extends ReactiveMongoRepository<ActorEntity, String> {
}
