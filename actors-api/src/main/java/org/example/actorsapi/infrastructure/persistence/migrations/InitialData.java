package org.example.actorsapi.infrastructure.persistence.migrations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.actorsapi.infrastructure.InfrastructureConstants;
import org.example.actorsapi.infrastructure.persistence.ActorEntity;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@ChangeUnit(id="InitialDataChangelog", order = "1")
@RequiredArgsConstructor
@Slf4j
public class InitialData {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    @Execution
    public void changeSet() throws IOException {
        val actors = objectMapper.readValue(getJson(), new TypeReference<List<ActorEntity>>() {});
        mongoTemplate.createCollection(InfrastructureConstants.ACTORS_COLLECTION_NAME);
        mongoTemplate.insertAll(actors);
    }

    @RollbackExecution
    public void rollback() {
        mongoTemplate.dropCollection(InfrastructureConstants.ACTORS_COLLECTION_NAME);
    }

    private String getJson() throws IOException, OutOfMemoryError {
        try (var resource = getClass().getClassLoader().getResourceAsStream("migrations/V_01_actors.json")) {
            return new String(Objects.requireNonNull(resource).readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
