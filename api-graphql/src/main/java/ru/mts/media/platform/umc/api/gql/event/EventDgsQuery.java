package ru.mts.media.platform.umc.api.gql.event;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import ru.mts.media.platform.umc.domain.entity.EventSot;
import ru.mts.media.platform.umc.domain.gql.types.Event;

import java.util.Optional;

@DgsComponent
@RequiredArgsConstructor
public class EventDgsQuery {
    private final EventSot sot;

    @DgsQuery
    public Event eventByReferenceId(@InputArgument Long id) {
        return Optional.of(id)
                .flatMap(sot::getEventById)
                .orElse(null);
    }
}
