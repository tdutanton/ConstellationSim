package constellation.Service.SpaceOperationCenterService.MissionRequest;

import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import constellation.Service.SpaceOperationCenterService.MissionRequest.MissionRequest;
import lombok.Getter;

@Getter
public class MissionRequestSatType extends MissionRequest {

  private final SatelliteType satelliteType;

  public MissionRequestSatType(String constellationName, SatelliteType satelliteType) {
    super(constellationName);
    this.satelliteType = satelliteType;
  }
}
