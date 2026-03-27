package constellation.Service.SpaceOperationCenterService.MissionRequest;

import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import constellation.Service.SpaceOperationCenterService.MissionRequest.MissionRequest;
import lombok.Getter;

@Getter
public class MissionRequestSatName extends MissionRequest {

  private final String satelliteName;

  public MissionRequestSatName(String constellationName, String satelliteName) {
    super(constellationName);
    this.satelliteName = satelliteName;
  }
}
