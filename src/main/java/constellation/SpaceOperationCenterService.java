package constellation;

import java.util.Objects;

public class SpaceOperationCenterService {
    private ConstellationRepository repository;


    public SpaceOperationCenterService(ConstellationRepository repository) {
        this.repository = repository;
    }

    private boolean isConstellationExists(SatelliteConstellation constellation) {
        if (constellation == null) return false;
        return (constellation.getConstellationName() != null) && !constellation.getConstellationName().isBlank();
    }

//    public createAndSaveConstellation(String name);
//    public addSatelliteToConstellation(String constellationName, Satellite satellite);
//    public executeConstellationMission(String constellationName);
//    public activateAllSatellites(String constellationName);
//    public showConstellationStatus(String constellationName)
}
