package ru.mts.media.platform.umc.domain.entity;

import ru.mts.media.platform.umc.domain.common.EntityEvent;
import ru.mts.media.platform.umc.domain.gql.types.Event;

public class EventSave extends EntityEvent<Event> {
    public EventSave(Event entity) {
        super(entity);
    }
}
