package ru.mts.media.platform.umc.domain.entity;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.SaveEventInput;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
interface EventDomainServiceMapper {
    Event patch(@MappingTarget Event src, SaveEventInput updates);

}
