CREATE TABLE satellites (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    satellite_type TEXT NOT NULL,
    constellation_id BIGINT REFERENCES constellations(id),

    -- Общие поля
    name TEXT NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT FALSE,
    battery_level DOUBLE PRECISION,

    -- Поля CommunicationSatellite (NULL для Imaging)
    bandwidth DOUBLE PRECISION,

    -- Поля ImagingSatellite (NULL для Communication)
    resolution DOUBLE PRECISION,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_satellite_name ON satellites(name);
CREATE INDEX idx_satellite_type ON satellites(satellite_type);