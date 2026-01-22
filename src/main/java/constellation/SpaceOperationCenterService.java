package constellation;

public class SpaceOperationCenterService {
    private final ConstellationRepository repository;


    public SpaceOperationCenterService(ConstellationRepository repository) {
        this.repository = repository;
    }

    private boolean isConstellationCorrect(SatelliteConstellation constellation) {
        if (constellation == null) return false;
        return (constellation.getConstellationName() != null) && !constellation.getConstellationName().isBlank();
    }

    private boolean isConstellationCorrect(String constellationName) {
        if (constellationName == null) return false;
        return !constellationName.isBlank();
    }

    private boolean isSatelliteCorrect(Satellite satellite) {
        if (satellite == null) return false;
        return (satellite.getName() != null) && !satellite.getName().isBlank();
    }

    public void createAndSaveConstellation(String name) {
        if (isConstellationCorrect(name)) {
            SatelliteConstellation constellation = new SatelliteConstellation(name);
            repository.addConstellation(constellation);
        }
    }

    public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
        repository.addSatellite(constellationName, satellite);
    }

    public void executeConstellationMission(String constellationName) {
        SatelliteConstellation constellation = constellationFromRepository(constellationName);
        if (constellation != null) {
            constellation.executeAllMissions();
        }
    }


    public void activateAllSatellites(String constellationName) {
        SatelliteConstellation constellation = constellationFromRepository(constellationName);
        if (constellation != null) {
            constellation.activateSatellites();
        }
    }

    private SatelliteConstellation constellationFromRepository(String constellationName) {
        if (isConstellationCorrect(constellationName)) {
            return repository.constellationByName(constellationName);
        }
        return null;
    }

    public void showConstellationStatus(String constellationName) {
        SatelliteConstellation constellation = constellationFromRepository(constellationName);
        if (constellation != null) {
            constellation.getSatellitesStatus();
        }
    }
}

