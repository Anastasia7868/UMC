package ru.mts.media.platform.umc.dao.postgres.venue;

import jakarta.persistence.*;
import lombok.Data;
import ru.mts.media.platform.umc.dao.postgres.common.FullExternalIdPk;
import ru.mts.media.platform.umc.dao.postgres.event.EventPgEntity;
import java.util.List;
@Entity
@Data
@IdClass(FullExternalIdPk.class)
@Table(name = "venue",
        indexes = {
                @Index(name = "idx_venue_referenceId",
                        columnList = "referenceId",
                        unique = true)
        })
@NamedEntityGraph(
        name = "VenuePgEntity.withEvents",
        attributeNodes = @NamedAttributeNode("events")
)
public class VenuePgEntity {
    @Id
    private String brand;

    @Id
    private String provider;

    @Id
    private String externalId;

    private String referenceId;

    private String name;

    @ManyToMany(mappedBy = "venues")
    private List<EventPgEntity> events;
}
