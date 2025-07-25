--  V2_create_event_table.sql
CREATE TABLE IF NOT EXISTS event
(
    id          BIGINT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    start_time  TIMESTAMP NOT NULL,
    end_time    TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS event_venue
(
    event_id         BIGINT NOT NULL,
    venue_brand      VARCHAR(255) NOT NULL,
    venue_provider   VARCHAR(255) NOT NULL,
    venue_external_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (event_id, venue_brand, venue_provider, venue_external_id),
    FOREIGN KEY (event_id) REFERENCES event(id),
    FOREIGN KEY (venue_brand, venue_provider, venue_external_id) REFERENCES venue(brand, provider, external_id)
);