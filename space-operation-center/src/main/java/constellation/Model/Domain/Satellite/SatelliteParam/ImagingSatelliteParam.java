package constellation.Model.Domain.Satellite.SatelliteParam;

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
