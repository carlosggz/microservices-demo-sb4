package org.example.actorsapi.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.example.actorsapi.application.LikesService;
import org.example.actorsapi.domain.models.LikeMovieEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
@EnableDiscoveryClient
@Slf4j
public class ApplicationConfiguration {

    @Bean
    public Consumer<Flux<LikeMovieEvent>> likesConsumer(LikesService likesService) {
        return flux -> flux
                .doOnNext(movies -> log.info("Received event: {}", movies))
                .map(LikeMovieEvent::movieId)
                .flatMap(likesService::likeActorsInMovie)
                .subscribe();
    }
}
