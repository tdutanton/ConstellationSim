package constellation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConstellationRepository {
    private Map<String, SatelliteConstellation> constellations;

    public ConstellationRepository() {
        this.constellations = new HashMap<>();
    }

    public void addConstellation(SatelliteConstellation constellation) {
        safetyAddConstellation(constellation);
    }

    private boolean isInRepository(SatelliteConstellation constellation) {
        return constellations.containsKey(constellation.getConstellationName());
    }

    private boolean isInRepository(String constellationName) {
        return constellations.containsKey(constellationName);
    }

    private void safetyAddConstellation(SatelliteConstellation constellation) {
        if (!isInRepository(constellation)) {
            constellations.put(constellation.getConstellationName(), constellation);
            return;
        }
            System.out.printf("Группировка %s уже существует в хранилище.%n", constellation.getConstellationName());
    }

    SatelliteConstellation constellationByName(String constellationName) {
        Objects.requireNonNull(constellationName, "Группировки не существует");
        return constellations.get(constellationName);
    }
}
