package ru.mts.media.platform.umc.dao.postgres.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.mts.media.platform.umc.dao.postgres.venue.VenuePgEntity;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "event")
@NamedEntityGraph(
        name = "EventPgEntity.withVenues",
        attributeNodes = @NamedAttributeNode("venues")
)
public class EventPgEntity {

    /**
     * PK для таблицы Entity. Стратегия - автоинкрементации
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * Название события
     */
    @NonNull
    private String name;

    private String startTime;

    private String endTime;

    @ManyToMany
    @JoinTable(
            name = "event_venue",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "venue_brand", referencedColumnName = "brand"),
                    @JoinColumn(name = "venue_provider", referencedColumnName = "provider"),
                    @JoinColumn(name = "venue_external_id", referencedColumnName = "external_id")
            }
    )
    private List<VenuePgEntity> venues;

}
