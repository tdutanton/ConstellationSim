package constellation.Service.SpaceOperationCenterService.MissionRequest;

import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Класс с параметрами, описывающий какие миссии и на каких спутниках/группировках должны быть
 * выполнены (например, группировка дистанционного зондирования Земли или группировка связи)
 */
@Getter
@RequiredArgsConstructor
public class MissionRequest {

  private final TargetType targetType;
  private final String constellationName;
  private final String satelliteName;

  public static MissionRequest forConstellation(String constellationName) {
    return new MissionRequest(TargetType.CONSTELLATION, constellationName, null);
  }

  public static MissionRequest forSatellite(String constellationName, String satelliteName) {
    return new MissionRequest(TargetType.SINGLE_SATELLITE, constellationName, satelliteName);
  }

  public enum TargetType {
    CONSTELLATION,
    SINGLE_SATELLITE
  }
}
