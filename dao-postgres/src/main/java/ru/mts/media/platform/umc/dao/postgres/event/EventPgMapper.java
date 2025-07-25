package ru.mts.media.platform.umc.dao.postgres.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.mts.media.platform.umc.dao.postgres.common.FullExternalIdPk;
import ru.mts.media.platform.umc.dao.postgres.venue.VenuePgEntity;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.FullExternalId;
import ru.mts.media.platform.umc.domain.gql.types.Venue;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
       unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EventPgMapper {

    @Mapping(target = "venues", source = "venues", qualifiedByName = "mapVenuesToModels")
    Event asModel(EventPgEntity eventPgEntity);
    @Mapping(target = "venues", source = "venues", qualifiedByName = "mapVenuesToPgEntities")
    EventPgEntity asEntity(Event event);

    @Named("mapVenuesToPgEntities")
    default List<VenuePgEntity> mapVenuesToPgEntities(List<Venue> venues) {
        if (venues == null) return null;
        return venues.stream()
                .map(this::asEntity)
                .toList();
    }

    @Mapping(target = "latestEvents", ignore = true)
    @Mapping(target = "externalId.brandId", source = "brand")
    @Mapping(target = "externalId.providerId", source = "provider")
    @Mapping(target = "externalId.externalId", source = "externalId")
    @Mapping(target = "id", source = "referenceId")
    Venue asModel(VenuePgEntity venuePg);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "referenceId", source = "id")
    @Mapping(target = "brand", source = "externalId.brandId")
    @Mapping(target = "provider", source = "externalId.providerId")
    @Mapping(target = "externalId", source = "externalId.externalId")
    VenuePgEntity asEntity(Venue venue);

    @Mapping(target = "brand", source = "brandId")
    @Mapping(target = "provider", source = "providerId")
    @Mapping(target = "externalId", source = "externalId")
    FullExternalIdPk asPk(FullExternalId fullExternalId);

    @Named("mapVenuesToModels")
    default List<Venue> mapVenuesToModels(List<VenuePgEntity> venuePgEntities) {
        if (venuePgEntities == null) return null;
        return venuePgEntities.stream()
                .map(this::asModel)
                .toList();
    }
}
