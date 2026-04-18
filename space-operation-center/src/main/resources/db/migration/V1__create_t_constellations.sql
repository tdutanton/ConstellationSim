CREATE TABLE constellations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    constellation_name TEXT NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ,

    CONSTRAINT name_not_empty CHECK (LENGTH(TRIM(constellation_name)) > 0)
);

CREATE INDEX idx_constellation_name ON constellations(constellation_name);