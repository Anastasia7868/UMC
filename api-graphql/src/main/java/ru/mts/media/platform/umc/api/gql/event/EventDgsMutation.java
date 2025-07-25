package ru.mts.media.platform.umc.api.gql.event;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import ru.mts.media.platform.umc.domain.entity.EventDomainService;
import ru.mts.media.platform.umc.domain.gql.types.Event;
import ru.mts.media.platform.umc.domain.gql.types.SaveEventInput;

@DgsComponent
@RequiredArgsConstructor
public class EventDgsMutation {
    private final EventDomainService domainService;

    @DgsQuery
    public Event saveEvent(@InputArgument Long id,
                           @InputArgument SaveEventInput input) {
        return domainService.save(id, input).getEntity();
    }
}
