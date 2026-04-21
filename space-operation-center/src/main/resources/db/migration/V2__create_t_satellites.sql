CREATE TABLE satellites (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    satellite_type TEXT NOT NULL,
    constellation_id BIGINT,

    -- Общие поля
    name TEXT NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT FALSE,
    constellation_id BIGINT REFERENCES constellations(id),
    battery_level DOUBLE PRECISION,

    -- Поля CommunicationSatellite (NULL для Imaging)
    bandwidth INTEGER,

    -- Поля ImagingSatellite (NULL для Communication)
    resolution DOUBLE PRECISION,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_satellite_type ON satellites(satellite_type);