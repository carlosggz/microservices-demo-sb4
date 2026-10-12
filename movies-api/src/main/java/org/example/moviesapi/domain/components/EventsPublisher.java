package org.example.moviesapi.domain.components;

import org.example.moviesapi.domain.models.DomainEvent;
import org.jspecify.annotations.NonNull;

public interface EventsPublisher {
    <T extends DomainEvent> void publish(@NonNull T domainEvent);
}
