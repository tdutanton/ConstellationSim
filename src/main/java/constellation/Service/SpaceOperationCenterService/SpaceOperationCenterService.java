package constellation.Service.SpaceOperationCenterService;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Model.Domain.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.CommunicationSatellite;
import constellation.Model.Domain.Satellite.ImagingSatellite;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import constellation.Service.ConstellationService.ConstellationService;
import constellation.Service.SatelliteService.SatelliteService;
import java.util.ArrayList;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceOperationCenterService {

  private final ConstellationService constellationService;
  private final SatelliteService satelliteService;

  private Map<SatelliteType, Class<? extends Satellite>> LUT_TYPES = Map.of(
      SatelliteType.COMMUNICATION, CommunicationSatellite.class,
      SatelliteType.IMAGE, ImagingSatellite.class
  );

  public boolean isType(Satellite satellite, SatelliteType expectedType) {
    Class<? extends Satellite> expectedClass = LUT_TYPES.get(expectedType);
    return expectedClass != null && expectedClass.isInstance(satellite);
  }

  public void addSatellite(AddSatelliteRequest request) {
    constellationService.createAndSaveConstellation(request.getConstellationName());
    for (SatelliteParam satelliteParam : request.getSatelliteParams()) {
      try {
        Satellite satellite = satelliteService.createSatellite(satelliteParam);
        constellationService.addSatelliteToConstellation(request.getConstellationName(), satellite);
      } catch (SpaceOperationException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public void executeMission(MissionRequest request) {
    for (String constellation : request.getConstellationNames()) {
      SatelliteConstellation currentConstellation = constellationService.constellationFromRepository(
          constellation);
      if (currentConstellation != null) {
        for (Satellite satellite : currentConstellation.getSatellites()) {
          if (isType(satellite, request.getSatelliteType())) {
            satelliteService.executeMission(satellite);
          }
        }
      }
    }
  }

  public void activateSatellites(ConstellationRequest request) {
    SatelliteConstellation constellation = constellationService.constellationFromRepository(
        request.getConstellationName());
    if (constellation != null) {
      constellationService.activateAllSatellites(constellation.getConstellationName());
    }
  }

  public void activateSatellites() {
    ArrayList<SatelliteConstellation> constellations = constellationService.constellations();
    if (constellations != null) {
      for (SatelliteConstellation constellation : constellations) {
        constellationService.activateAllSatellites(constellation.getConstellationName());
      }
    }
  }

  public void showConstellationStatus(ConstellationRequest request) {
    SatelliteConstellation constellation = constellationService.constellationFromRepository(
        request.getConstellationName());
    if (constellation != null) {
      constellationService.showConstellationStatus(constellation.getConstellationName());
    }
  }

  public void showConstellationsStatuses() {
    ArrayList<SatelliteConstellation> constellations = constellationService.constellations();
    if (constellations != null) {
      for (SatelliteConstellation constellation : constellations) {
        constellationService.showConstellationStatus(constellation.getConstellationName());
      }
    }
  }
}
