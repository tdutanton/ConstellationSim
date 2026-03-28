package missionScheduler.Domain.MissionRequest;

import lombok.Getter;
import missionScheduler.Domain.TargetType.TargetType;

/**
 * Класс с параметрами, описывающий какие миссии и на каких спутниках/группировках должны быть
 * выполнены (например, группировка дистанционного зондирования Земли или группировка связи)
 */
@Getter
public abstract class MissionRequest {

  protected final String constellationName;
  protected final TargetType targetType;

  public MissionRequest(String constellationName, TargetType targetType) {
    this.constellationName = constellationName;
    this.targetType = targetType;
  }
}
