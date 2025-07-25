package ru.mts.media.platform.umc.dao.postgres.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mts.media.platform.umc.dao.postgres.venue.VenuePgEntity;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.FullExternalId;
import ru.mts.media.platform.umc.domain.gql.types.Venue;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class EventPgMapperImplTest {


    private EventPgMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EventPgMapperImpl();
    }

    @Test
    void testAsModel_Success() {
        EventPgEntity entity = new EventPgEntity();
        entity.setId("1");
        entity.setName("Test Event");
        entity.setStartTime("2025-07-25T16:31:00"); // 04:31 PM CEST
        entity.setEndTime("2025-07-25T18:31:00");  // 06:31 PM CEST

        VenuePgEntity venueEntity = new VenuePgEntity();
        venueEntity.setBrand("TestBrand");
        venueEntity.setProvider("TestProvider");
        venueEntity.setExternalId("123");
        venueEntity.setReferenceId("ref123");
        venueEntity.setName("Test Venue");
        entity.setVenues(List.of(venueEntity));

        // Act
        Event result = mapper.asModel(entity);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Test Event", result.getName());
        assertEquals("2025-07-25T16:31:00", result.getStartTime());
        assertEquals("2025-07-25T18:31:00", result.getEndTime());
        assertNotNull(result.getVenues());
        assertEquals(1, result.getVenues().size());
        Venue venue = result.getVenues().get(0);
        assertEquals("TestBrand", venue.getExternalId().getBrandId());
        assertEquals("TestProvider", venue.getExternalId().getProviderId());
        assertNotNull(venue.getExternalId());
        assertEquals("123", venue.getExternalId().getExternalId());
        assertEquals("Test Venue", venue.getName());
    }

    @Test
    void testAsEntity_Success() {
        Event event = new Event();
        event.setId("1");
        event.setName("Test Event");
        event.setStartTime("2025-07-25T16:31:00"); // 04:31 PM CEST
        event.setEndTime("2025-07-25T18:31:00");  // 06:31 PM CEST

        Venue venue = new Venue();
        venue.setExternalId(FullExternalId.newBuilder()
                .brandId("TestBrand")
                .providerId("TestProvider")
                .externalId("123")
                .build());
        venue.setName("Test Venue");
        event.setVenues(List.of(venue));

        // Act
        EventPgEntity result = mapper.asEntity(event);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Test Event", result.getName());
        assertEquals("2025-07-25T16:31:00", result.getStartTime());
        assertEquals("2025-07-25T18:31:00", result.getEndTime());
        assertNotNull(result.getVenues());
        assertEquals(1, result.getVenues().size());
        VenuePgEntity venueEntity = result.getVenues().get(0);
        assertEquals("TestBrand", venueEntity.getBrand());
        assertEquals("TestProvider", venueEntity.getProvider());
        assertEquals("123", venueEntity.getExternalId());
        assertEquals("Test Venue", venueEntity.getName());
    }

    @Test
    void testAsModel_NullEntity() {
        EventPgEntity entity = null;

        // Act
        Event result = mapper.asModel(entity);

        // Assert
        assertNull(result);
    }

    @Test
    void testAsEntity_NullEvent() {
        Event event = null;

        // Act
        EventPgEntity result = mapper.asEntity(event);

        // Assert
        assertNull(result);
    }

    @Test
    void testMapVenuesToPgEntities_Success() {
        FullExternalId externalId = new FullExternalId();
        externalId.setExternalId("123");
        externalId.setBrandId("TestBrand");
        externalId.setProviderId("TestProvider");

        Venue venue = new Venue();
        venue.setExternalId(FullExternalId.newBuilder()
                .brandId("TestBrand")
                .providerId("TestProvider")
                .externalId("123")
                .build());

        venue.setName("Test Venue");
        List<Venue> venues = List.of(venue);

        // Act
        List<VenuePgEntity> result = mapper.mapVenuesToPgEntities(venues);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        VenuePgEntity venueEntity = result.get(0);
        assertEquals("TestBrand", venueEntity.getBrand());
        assertEquals("TestProvider", venueEntity.getProvider());
        assertEquals("123", venueEntity.getExternalId());
        assertEquals("Test Venue", venueEntity.getName());
    }

    @Test
    void testMapVenuesToModels_Success() {
        VenuePgEntity venueEntity = new VenuePgEntity();

        venueEntity.setBrand("TestBrand");
        venueEntity.setProvider("TestProvider");
        venueEntity.setExternalId("123");
        venueEntity.setReferenceId("ref123");
        venueEntity.setName("Test Venue");
        List<VenuePgEntity> venuePgEntities = List.of(venueEntity);

        // Act
        List<Venue> result = mapper.mapVenuesToModels(venuePgEntities);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        Venue venue = result.get(0);
        assertEquals("TestBrand", venue.getExternalId().getBrandId());
        assertEquals("TestProvider", venue.getExternalId().getProviderId());
        assertNotNull(venue.getExternalId());
        assertEquals("123", venue.getExternalId().getExternalId());
        assertEquals("Test Venue", venue.getName());
    }

    @Test
    void testMapVenuesToPgEntities_NullList() {
        List<Venue> venues = null;

        // Act
        List<VenuePgEntity> result = mapper.mapVenuesToPgEntities(venues);

        // Assert
        assertNull(result);
    }

    @Test
    void testMapVenuesToModels_NullList() {
        List<VenuePgEntity> venuePgEntities = null;

        // Act
        List<Venue> result = mapper.mapVenuesToModels(venuePgEntities);

        // Assert
        assertNull(result);
    }
}