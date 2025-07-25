package ru.mts.media.platform.umc.domain.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.SaveEventInput;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class EventDomainService {
    private final ApplicationEventPublisher eventPublisher;
    private final EventSot sot;
    private final EventDomainServiceMapper mapper;

    public EventSave save(Long id, SaveEventInput input) {

        var evt = Optional.of(id)
                .flatMap(sot::getEventById)
                .map(applyPatch(input))
                .map(EventSave::new)
                .orElse(null);

        eventPublisher.publishEvent(evt);

        return evt;
    }

    private Function<Event, Event> applyPatch(SaveEventInput updates) {
        return x -> mapper.patch(x, updates);
    }
}
