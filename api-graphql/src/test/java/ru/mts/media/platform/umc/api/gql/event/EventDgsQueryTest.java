package ru.mts.media.platform.umc.api.gql.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mts.media.platform.umc.domain.entity.EventSot;
import ru.mts.media.platform.umc.domain.gql.types.Event;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventDgsQueryTest {

    @InjectMocks
    private EventDgsQuery eventDgsQuery;

    @Mock
    private EventSot sot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEventByReferenceId_Success() {
        Long id = 1L;
        Event mockEvent = new Event();
        mockEvent.setId(id.toString());
        mockEvent.setName("Test Event");
        mockEvent.setStartTime("2025-07-25T10:01:00");
        mockEvent.setEndTime("2025-07-25T12:01:00");  // 12:01 PM EEST

        when(sot.getEventById(id)).thenReturn(Optional.of(mockEvent));

        // Act
        Event result = eventDgsQuery.eventByReferenceId(id);

        // Assert
        assertNotNull(result);
        assertEquals(id.toString(), result.getId());
        assertEquals("Test Event", result.getName());
        assertEquals("2025-07-25T10:01:00", result.getStartTime());
        assertEquals("2025-07-25T12:01:00", result.getEndTime());
        verify(sot, times(1)).getEventById(id);
    }

    @Test
    void testEventByReferenceId_NotFound() {
        Long id = 2L;
        when(sot.getEventById(id)).thenReturn(Optional.empty());

        // Act
        Event result = eventDgsQuery.eventByReferenceId(id);

        // Assert
        assertNull(result);
        verify(sot, times(1)).getEventById(id);
    }

    @Test
    void testEventByReferenceId_NullId() {
        Long id = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> eventDgsQuery.eventByReferenceId(id));
        verify(sot, never()).getEventById(anyLong());
    }
}