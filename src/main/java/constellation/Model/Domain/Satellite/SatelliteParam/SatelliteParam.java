package constellation.Model.Domain.Satellite.SatelliteParam;

import constellation.Model.Domain.Satellite.SatelliteType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class SatelliteParam {

  protected SatelliteType type;
  protected String name;
  protected double batteryLevel;
}
