package ru.mts.media.platform.umc.dao.postgres.event.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.mts.media.platform.umc.dao.postgres.event.EventPgMapper;
import ru.mts.media.platform.umc.dao.postgres.event.EventPgRepository;
import ru.mts.media.platform.umc.domain.entity.EventSave;
import ru.mts.media.platform.umc.domain.entity.EventSot;
import ru.mts.media.platform.umc.domain.gql.types.Event;

import java.util.Optional;

import static java.text.MessageFormat.format;

@Component
@RequiredArgsConstructor
public class EventPgDao implements EventSot {

    private final EventPgRepository repository;
    private final EventPgMapper mapper;

    public Optional<Event> getEventById(Long id) {

        return Optional.of(id)
                .flatMap(repository::findById)
                .map(mapper::asModel)
                .map(Optional::of)
                .orElseThrow(() -> new EntityNotFoundException(format("event with id {0} was not found", id)));
    }

    @EventListener
    public void handleEventCreatedEvent(EventSave evt) {
        evt.unwrap()
                .map(mapper::asEntity)
                .ifPresent(repository::save);
    }
}
