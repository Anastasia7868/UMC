package ru.mts.media.platform.umc.dao.postgres.event;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mts.media.platform.umc.domain.gql.types.Event;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями {@link Event}.
 * @see JpaRepository
 * @see Event
 */
public interface EventPgRepository extends JpaRepository<EventPgEntity, Long> {
    @EntityGraph(value = "EventPgEntity.withVenues", type = EntityGraph.EntityGraphType.LOAD)
    Optional<EventPgEntity> findById(@NonNull Long id);
}