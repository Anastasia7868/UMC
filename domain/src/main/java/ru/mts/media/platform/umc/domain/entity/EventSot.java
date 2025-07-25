package ru.mts.media.platform.umc.domain.entity;

import ru.mts.media.platform.umc.domain.gql.types.Event;

import java.util.Optional;

public interface EventSot {

    Optional<Event> getEventById(Long id);

}
