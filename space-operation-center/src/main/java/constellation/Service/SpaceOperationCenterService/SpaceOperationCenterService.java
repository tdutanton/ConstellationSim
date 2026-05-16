package constellation.Service.SpaceOperationCenterService;

import constellation.Kafka.SatelliteEventPublisher;
import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Model.Domain.Exception.SpaceOperationException;
import constellation.Model.Domain.LogExecutionTime.LogExecutionTime;
import constellation.Model.Domain.Satellite.CommunicationSatellite;
import constellation.Model.Domain.Satellite.ImagingSatellite;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import constellation.Service.ConstellationService.DTO.ConstellationStatusDTO;
import constellation.Service.ConstellationService.ServiceDB.ConstellationService;
import constellation.Service.SatelliteService.SatelliteService;
import constellation.Service.SpaceOperationCenterService.MissionRequest.MissionRequest;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceOperationCenterService {

  private final ConstellationService constellationService;
  private final SatelliteService satelliteService;
  private final SatelliteEventPublisher eventPublisher;

  private final Map<SatelliteType, Class<? extends Satellite>> LUT_TYPES = Map.of(
      SatelliteType.COMMUNICATION, CommunicationSatellite.class,
      SatelliteType.IMAGE, ImagingSatellite.class
  );

  public boolean isType(Satellite satellite, SatelliteType expectedType) {
    Class<? extends Satellite> expectedClass = LUT_TYPES.get(expectedType);
    return expectedClass != null && expectedClass.isInstance(satellite);
  }

  @LogExecutionTime
  @Transactional
  public void addSatellite(AddSatelliteRequest request) {
    constellationService.createAndSaveConstellation(request.getConstellationName());
    for (SatelliteParam satelliteParam : request.getSatelliteParams()) {
      try {
        Satellite satellite = satelliteService.createSatellite(satelliteParam);
        constellationService.addSatelliteToConstellation(request.getConstellationName(), satellite);
        eventPublisher.publishSatelliteAdded(satellite);
      } catch (SpaceOperationException e) {
        System.out.println(e.getMessage());
      }
    }
  }


  @LogExecutionTime
  @Transactional
  public void executeMission(MissionRequest request) {
    SatelliteConstellation currentConstellation = constellationService.constellationFromRepository(
        request.getConstellationName());
    if (currentConstellation != null) {
      switch (request.getTargetType()) {
        case SINGLE_SATELLITE -> {
          Satellite satellite = constellationService.satelliteByName(
              currentConstellation.getConstellationName(), request.getSatelliteName());
          satelliteService.executeMission(satellite);
        }
        case CONSTELLATION -> {
          for (Satellite satellite : currentConstellation.getSatellites()) {
            satelliteService.executeMission(satellite);
          }
        }
      }
    }
  }


  @LogExecutionTime
  @Transactional
  public void activateSatellites(ConstellationRequest request) {
    SatelliteConstellation constellation = constellationService.constellationFromRepository(
        request.getConstellationName());
    if (constellation != null) {
      constellationService.activateAllSatellites(constellation.getConstellationName());
    }
  }

  @LogExecutionTime
  @Transactional
  public void activateSatellites() {
    ArrayList<SatelliteConstellation> constellations = constellationService.constellations();
    if (constellations != null) {
      for (SatelliteConstellation constellation : constellations) {
        constellationService.activateAllSatellites(constellation.getConstellationName());
      }
    }
  }

  @LogExecutionTime
  @Transactional
  public void showConstellationStatus(ConstellationRequest request) {
    SatelliteConstellation constellation = constellationService.constellationFromRepository(
        request.getConstellationName());
    if (constellation != null) {
      constellationService.showConstellationStatus(constellation.getConstellationName());
    }
  }

  @LogExecutionTime
  @Transactional
  public void showConstellationsStatuses() {
    ArrayList<SatelliteConstellation> constellations = constellationService.constellations();
    if (constellations != null) {
      for (SatelliteConstellation constellation : constellations) {
        constellationService.showConstellationStatus(constellation.getConstellationName());
      }
    }
  }

  @LogExecutionTime
  @Transactional
  public List<ConstellationStatusDTO> overview() {
    ArrayList<SatelliteConstellation> constellations = constellationService.constellations();
    List<ConstellationStatusDTO> result = new ArrayList<>();
    if (constellations != null) {
      for (SatelliteConstellation constellation : constellations) {
        ConstellationStatusDTO dto = new ConstellationStatusDTO();
        dto.setConstellationName(constellation.getConstellationName());
        dto.setSatellitesCount(constellation.getSatellites().size());
        for (Satellite satellite : constellation.getSatellites()) {
          dto.getSatellitesStatus().add(satellite.toString());
        }
        result.add(dto);
      }
    }
    return result;
  }

  @LogExecutionTime
  @Transactional
  public boolean deleteSatellite(String constellationName, String satelliteName) {
    Satellite satellite = constellationService.satelliteByName(constellationName, satelliteName);
    if (satellite == null) {
      return false;
    }
    Long satelliteId = satellite.getId();  // сохраняем ID до удаления
    String satName = satellite.getName();
    boolean result = constellationService.deleteSatellite(constellationName, satelliteName);
    if (result) {
      eventPublisher.publishSatelliteRemoved(satelliteId, satName);
    }
    return result;
  }
}
