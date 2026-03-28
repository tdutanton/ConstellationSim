package missionScheduler.Domain.MissionRequest;

import lombok.Getter;
import missionScheduler.Domain.SatelliteType.SatelliteType;
import missionScheduler.Domain.TargetType.TargetType;

@Getter
public class MissionRequestSatType extends MissionRequest {

  private final SatelliteType satelliteType;

  public MissionRequestSatType(TargetType targetType, String constellationName,
      SatelliteType satelliteType) {
    super(constellationName, targetType);
    this.satelliteType = satelliteType;
  }
}
