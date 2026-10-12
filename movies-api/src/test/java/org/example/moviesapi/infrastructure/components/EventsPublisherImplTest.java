package org.example.moviesapi.infrastructure.components;

import lombok.val;
import org.example.moviesapi.domain.models.LikeMovieEvent;
import org.example.moviesapi.domain.models.MovieDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventsPublisherImplTest {

    @Mock
    StreamBridge streamBridge;

    @InjectMocks
    EventsPublisherImpl eventsPublisher;

    @Captor
    ArgumentCaptor<Message<LikeMovieEvent>> captor;

    @Test
    void publishEventSendsMessage() {
        //given
        val givenMovie = new MovieDto("1", "Inception", 2010, "imbd", 0);
        val givenEvent = new LikeMovieEvent(givenMovie);

        //when
        eventsPublisher.publish(givenEvent);

        //then
        verify(streamBridge).send(eq(EventsPublisherImpl.BINDING), captor.capture());
        val message = captor.getValue();
        assertNotNull(message);
        assertEquals(givenEvent, message.getPayload());
        assertTrue(message.getHeaders().containsKey(EventsPublisherImpl.ROUTING_KEY));
    }

}