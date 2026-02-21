package constellation.Domain.Satellite.SatelliteParam;

import constellation.Domain.Satellite.SatelliteType;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class CommunicationSatelliteParam extends SatelliteParam {

  private final double bandwidth;

  public CommunicationSatelliteParam(SatelliteType aType, String aName, double aBatteryLevel,
      double aBandwidth) {
    super(aType, aName, aBatteryLevel);
    bandwidth = aBandwidth;
  }
}
