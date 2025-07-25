package ru.mts.media.platform.umc.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.SaveEventInput;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EventDomainServiceMapperImplTest {

    private static final String ID = "1";
    private EventDomainServiceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EventDomainServiceMapperImpl();
    }

    @Test
    void testPatch_Success() {

        Event src = new Event();
        src.setId(ID);
        src.setName("Original Event");
        src.setStartTime("2025-07-25T09:00:00");
        src.setEndTime("2025-07-25T11:00:00");

        SaveEventInput updates = new SaveEventInput();
        updates.setName("Updated Event");
        updates.setStartTime("2025-07-25T12:57:00"); // 12:57 PM CEST
        updates.setEndTime("2025-07-25T14:57:00");  // 02:57 PM CEST
        updates.setVenueReferenceIds(List.of("ref123"));

        // Act
        Event result = mapper.patch(src, updates);

        // Assert
        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals("Updated Event", result.getName());
        assertEquals("2025-07-25T12:57:00", result.getStartTime());
        assertEquals("2025-07-25T14:57:00", result.getEndTime());
    }

    @Test
    void testPatch_NullUpdates() {
        Event src = new Event();
        src.setId(ID);
        src.setName("Original Event");
        src.setStartTime("2025-07-25T09:00:00");
        src.setEndTime("2025-07-25T11:00:00");

        // Act
        Event result = mapper.patch(src, null);

        // Assert
        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals("Original Event", result.getName());
        assertEquals("2025-07-25T09:00:00", result.getStartTime());
        assertEquals("2025-07-25T11:00:00", result.getEndTime());
    }
}