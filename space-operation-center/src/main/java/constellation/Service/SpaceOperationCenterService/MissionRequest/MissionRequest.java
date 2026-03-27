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
public abstract class MissionRequest {

  protected final String constellationName;

  public MissionRequest(String constellationName) {
    this.constellationName = constellationName;
  }
}
