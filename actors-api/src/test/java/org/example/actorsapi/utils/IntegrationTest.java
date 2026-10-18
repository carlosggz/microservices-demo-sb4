package org.example.actorsapi.utils;

import org.example.actorsapi.domain.components.ActorsStore;
import org.example.actorsapi.infrastructure.mappers.ActorMapper;
import org.example.actorsapi.infrastructure.persistence.ActorsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.data.mongodb.test.autoconfigure.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
@AutoConfigureDataMongo
@EnableAutoConfiguration()
public abstract class IntegrationTest {

    @MockitoSpyBean
    protected ActorsStore actorsStore;

    @Autowired
    protected ActorsRepository actorsRepository;

    @Autowired
    protected ActorMapper mapper;

    @BeforeEach
    void setUp() {
        actorsRepository.deleteAll().block();
    }
}
