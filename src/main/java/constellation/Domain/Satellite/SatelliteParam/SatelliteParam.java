package constellation.Domain.Satellite.SatelliteParam;

import constellation.Domain.Satellite.SatelliteType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class SatelliteParam {

  protected SatelliteType type;
  protected String name;
  protected double batteryLevel;
}
