package org.example.actorsapi.infrastructure.api;

import lombok.RequiredArgsConstructor;
import org.example.actorsapi.application.*;
import org.example.actorsapi.domain.exceptions.ActorAlreadyExistsException;
import org.example.actorsapi.domain.exceptions.UnknownActorException;
import org.example.actorsapi.domain.models.ActorDto;
import org.example.actorsapi.domain.models.ActorInfoDto;
import org.example.actorsapi.domain.models.ActorsFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/actors")
@RestController
@RequiredArgsConstructor
public class ActorsController {
    private final AddActorsService addActorService;
    private final DeleteActorsService deleteActorService;
    private final UpdateActorsService updateActorsService;
    private final DetailsActorService detailsActorService;
    private final ListActorsService listActorsService;

    @GetMapping
    public Flux<ActorInfoDto> getActors(@ModelAttribute ActorsFilter filter) {
        return listActorsService.getActorsList(filter);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ActorDto>> getDetails(@PathVariable("id") String id) {
        return detailsActorService
                .getActorById(id)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(e instanceof UnknownActorException
                        ? ResponseEntity.notFound().build()
                        : ResponseEntity.internalServerError().build()
                ));
    }

    @PostMapping
    public Mono<ResponseEntity<ActorDto>> add(@RequestBody ActorDto ActorDto) {
        return addActorService
                .addActor(ActorDto)
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(ActorDto))
                .onErrorResume(e -> Mono.just(e instanceof ActorAlreadyExistsException
                        ? ResponseEntity.badRequest().build()
                        : ResponseEntity.internalServerError().build()
                ));
    }

    @PutMapping
    public Mono<ResponseEntity<ActorDto>> update(@RequestBody ActorDto ActorDto) {
        return updateActorsService
                .updateActor(ActorDto)
                .thenReturn(ResponseEntity.ok(ActorDto))
                .onErrorResume(e -> Mono.just(e instanceof UnknownActorException
                        ? ResponseEntity.notFound().build()
                        : ResponseEntity.internalServerError().build()
                ));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        return deleteActorService
                .deleteActor(id)
                .thenReturn(ResponseEntity.ok().<Void>build())
                .onErrorResume(e -> Mono.just(e instanceof UnknownActorException
                        ? ResponseEntity.notFound().build()
                        : ResponseEntity.internalServerError().build()
                ));
    }
}
