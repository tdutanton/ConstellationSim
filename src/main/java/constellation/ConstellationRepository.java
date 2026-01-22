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

    public void addSatellite(String constellationName, Satellite satellite) {
        safetyAddSatellite(constellationName, satellite);
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
        Objects.requireNonNull(constellationName, "Группировки" + constellationName + "не существует");
        return constellations.get(constellationName);
    }

    private void safetyAddSatellite(String constellationName, Satellite satellite) {
        SatelliteConstellation constellation = constellationByName(constellationName);
        if (isInRepository(constellation)) {
            constellation.addSatellite(satellite);
            return;
        }
        System.out.println("Группировки" + constellationName + "не существует");
    }
}
