CREATE TABLE constellations (
    constellation_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    constellation_name TEXT NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,

    CONSTRAINT name_not_empty CHECK (LENGTH(TRIM(constellation_name)) > 0)
);

CREATE INDEX idx_constellation_name ON constellations(constellation_name);

CREATE TABLE satellites (
    satellite_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    type TEXT NOT NULL,
    constellation_id BIGINT NOT NULL REFERENCES constellations(constellation_id),

    is_active BOOLEAN DEFAULT FALSE NOT NULL,
    battery_level DOUBLE PRECISION NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_satellite_name ON satellites(satellite_name);
CREATE INDEX idx_sat_constellation ON satellites(constellation_id);

CREATE TABLE imaging_satellites (
  satellite_id BIGINT PRIMARY KEY REFERENCES satellites(satellite_id) ON DELETE CASCADE,
  resolution DOUBLE PRECISION NOT NULL,
  photos_taken INTEGER NOT NULL
)

CREATE TABLE communication_satellites (
  satellite_id BIGINT PRIMARY KEY REFERENCES satellites(satellite_id) ON DELETE CASCADE,
  bandwidth DOUBLE PRECISION NOT NULL
)