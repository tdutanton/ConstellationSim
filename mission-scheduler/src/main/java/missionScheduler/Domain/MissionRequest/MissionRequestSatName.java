package missionScheduler.Domain.MissionRequest;

import lombok.Getter;
import missionScheduler.Domain.TargetType.TargetType;

@Getter
public class MissionRequestSatName extends MissionRequest {

  private final String satelliteName;

  public MissionRequestSatName(TargetType targetType, String constellationName,
      String satelliteName) {
    super(constellationName, targetType);
    this.satelliteName = satelliteName;
  }
}
