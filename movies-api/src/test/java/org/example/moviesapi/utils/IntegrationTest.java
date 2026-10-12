package org.example.moviesapi.utils;

import org.example.moviesapi.domain.components.MoviesStore;
import org.example.moviesapi.infrastructure.persistence.MoviesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
public abstract class IntegrationTest {

    @MockitoSpyBean
    protected MoviesStore moviesStore;

    @Autowired
    protected MoviesRepository moviesRepository;

    @BeforeEach
    void setUp() {
        moviesRepository.deleteAll();
    }
}
