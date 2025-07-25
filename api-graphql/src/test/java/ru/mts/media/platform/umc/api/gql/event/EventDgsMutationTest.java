package ru.mts.media.platform.umc.api.gql.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mts.media.platform.umc.domain.entity.EventDomainService;
import ru.mts.media.platform.umc.domain.entity.EventSave;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.SaveEventInput;

import static graphql.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventDgsMutationTest {

    @Mock
    private EventDomainService domainService;

    @InjectMocks
    private EventDgsMutation eventDgsMutation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEvent_Success() {
        Long id = 1L;
        SaveEventInput input = new SaveEventInput();
        input.setName("Test Event");

        Event mockEvent = new Event();
        mockEvent.setId(id.toString());
        mockEvent.setName("Test Event");
        mockEvent.setStartTime("2025-07-24T20:26:00");
        mockEvent.setEndTime("2025-07-24T22:26:00");

        when(domainService.save(id, input)).thenReturn(new EventSave(mockEvent));

        Event result = eventDgsMutation.saveEvent(id, input);

        assertNotNull(result);
        assertEquals(id, Long.parseLong(result.getId()));
        assertEquals("Test Event", result.getName());
        assertEquals("2025-07-24T20:26:00", result.getStartTime());
        assertEquals("2025-07-24T22:26:00", result.getEndTime());
        verify(domainService, times(1)).save(id, input);
    }

    @Test
    void testSaveEvent_NullInput() {
        Long id = 1L;
        SaveEventInput input = null;

        assertThrows(NullPointerException.class, () -> eventDgsMutation.saveEvent(id, input));
        verify(domainService, never()).save(anyLong(), any(SaveEventInput.class));
    }

    @Test
    void testSaveEvent_NullId() {
        // Arrange
        Long id = null;
        SaveEventInput input = new SaveEventInput();
        input.setName("Test Event");

        // Act & Assert
        assertThrows(NullPointerException.class, () -> eventDgsMutation.saveEvent(id, input));
        verify(domainService, never()).save(anyLong(), any(SaveEventInput.class));
    }
}
