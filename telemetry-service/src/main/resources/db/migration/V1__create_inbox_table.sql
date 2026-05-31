CREATE TABLE inbox_events (
    event_id TEXT PRIMARY KEY,
    aggregate_id BIGINT NOT NULL,
    event_type TEXT NOT NULL,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_inbox_aggregate_id ON inbox_events(aggregate_id);
