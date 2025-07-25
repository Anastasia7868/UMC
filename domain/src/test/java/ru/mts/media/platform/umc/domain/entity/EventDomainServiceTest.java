package ru.mts.media.platform.umc.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.SaveEventInput;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventDomainServiceTest {

    @InjectMocks
    private EventDomainService eventDomainService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private EventSot sot;

    @Mock
    private EventDomainServiceMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_Success() {
        Long id = 1L;
        SaveEventInput input = new SaveEventInput();
        input.setName("Updated Event");

        Event existingEvent = new Event();
        existingEvent.setId(id.toString());
        existingEvent.setName("Original Event");
        existingEvent.setStartTime("2025-07-25T09:00:00");
        existingEvent.setEndTime("2025-07-25T11:00:00");

        Event patchedEvent = new Event();
        patchedEvent.setId(id.toString());
        patchedEvent.setName("Updated Event");
        patchedEvent.setStartTime("2025-07-25T10:21:00");
        patchedEvent.setEndTime("2025-07-25T12:21:00");

        when(sot.getEventById(id)).thenReturn(Optional.of(existingEvent));
        when(mapper.patch(existingEvent, input)).thenReturn(patchedEvent);

        // Act
        EventSave result = eventDomainService.save(id, input);

        // Assert
        assertNotNull(result);
        assertEquals(patchedEvent, result.getEntity());
        verify(sot, times(1)).getEventById(id);
        verify(mapper, times(1)).patch(existingEvent, input);
        verify(eventPublisher, times(1)).publishEvent(result);
    }

    @Test
    void testSave_EventNotFound() {
        // Arrange
        Long id = 2L;
        SaveEventInput input = new SaveEventInput();
        input.setName("New Event");

        when(sot.getEventById(id)).thenReturn(Optional.empty());

        // Act
        EventSave result = eventDomainService.save(id, input);

        // Assert
        assertNull(result);
        verify(sot, times(1)).getEventById(id);
        verify(mapper, never()).patch(any(Event.class), any(SaveEventInput.class));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void testSave_NullInput() {
        // Arrange

        // Act & Assert
        assertThrows(NullPointerException.class, () -> eventDomainService.save(null, null));
        verify(sot, never()).getEventById(anyLong());
        verify(mapper, never()).patch(any(Event.class), any(SaveEventInput.class));
        verify(eventPublisher, never()).publishEvent(any());
    }

}