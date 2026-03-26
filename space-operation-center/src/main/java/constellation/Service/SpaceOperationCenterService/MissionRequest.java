package constellation.Service.SpaceOperationCenterService;

import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Класс с параметрами, описывающий какие миссии и на каких спутниках/группировках должны быть
 * выполнены (например, группировка дистанционного зондирования Земли или группировка связи)
 */
@RequiredArgsConstructor
@Getter
public class MissionRequest {

  private final List<String> constellationNames;
  private final SatelliteType satelliteType;
}
