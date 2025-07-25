package ru.mts.media.platform.umc.dao.postgres.event.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mts.media.platform.umc.dao.postgres.event.EventPgEntity;
import ru.mts.media.platform.umc.dao.postgres.event.EventPgMapper;
import ru.mts.media.platform.umc.dao.postgres.event.EventPgRepository;
import ru.mts.media.platform.umc.domain.entity.EventSave;
import ru.mts.media.platform.umc.domain.gql.types.Event;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventPgDaoTest {

    @InjectMocks
    private EventPgDao eventPgDao;

    @Mock
    private EventPgRepository repository;

    @Mock
    private EventPgMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEventById_Success() {
        // Arrange
        Long id = 1L;
        EventPgEntity entity = new EventPgEntity();
        entity.setId(id.toString());
        entity.setName("Test Event");
        entity.setStartTime("2025-07-25T14:43:00"); // 02:43 PM CEST
        entity.setEndTime("2025-07-25T16:43:00");  // 04:43 PM CEST

        Event model = new Event();
        model.setId(id.toString());
        model.setName("Test Event");
        model.setStartTime("2025-07-25T14:43:00");
        model.setEndTime("2025-07-25T16:43:00");

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.asModel(entity)).thenReturn(model);

        // Act
        Optional<Event> result = eventPgDao.getEventById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(model, result.get());
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).asModel(entity);
    }

    @Test
    void testGetEventById_NotFound() {
        Long id = 2L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> eventPgDao.getEventById(id));
        assertEquals("event with id {0} was not found", exception.getMessage().replace(String.valueOf(id), "{0}"));
        verify(repository, times(1)).findById(id);
        verify(mapper, never()).asModel(any(EventPgEntity.class));
    }

    @Test
    void testHandleEventCreatedEvent_Success() {
        long id = 1L;
        Event event = new Event();
        event.setId(Long.toString(id));
        event.setName("Test Event");
        event.setStartTime("2025-07-25T14:43:00");
        event.setEndTime("2025-07-25T16:43:00");

        EventPgEntity entity = new EventPgEntity();
        entity.setId(Long.toString(id));
        entity.setName("Test Event");
        entity.setStartTime("2025-07-25T14:43:00");
        entity.setEndTime("2025-07-25T16:43:00");

        EventSave eventSave = new EventSave(event);

        when(mapper.asEntity(event)).thenReturn(entity);

        // Act
        eventPgDao.handleEventCreatedEvent(eventSave);

        // Assert
        verify(mapper, times(1)).asEntity(event);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void testHandleEventCreatedEvent_NullEvent() {
        EventSave eventSave = new EventSave(null);

        // Act
        eventPgDao.handleEventCreatedEvent(eventSave);

        // Assert
        verify(mapper, never()).asEntity(any(Event.class));
        verify(repository, never()).save(any());
    }
}