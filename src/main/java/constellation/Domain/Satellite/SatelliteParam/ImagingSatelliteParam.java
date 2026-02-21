package constellation.Domain.Satellite.SatelliteParam;

import constellation.Domain.Satellite.SatelliteType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ImagingSatelliteParam extends SatelliteParam {

  private final double resolution;

  public ImagingSatelliteParam(SatelliteType aType, String aName, double aBatteryLevel,
      double aResolution) {
    super(aType, aName, aBatteryLevel);
    resolution = aResolution;
  }
}
