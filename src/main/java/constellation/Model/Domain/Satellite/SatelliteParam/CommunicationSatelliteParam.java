package constellation.Model.Domain.Satellite.SatelliteParam;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CommunicationSatelliteParam extends SatelliteParam {

  private final double bandwidth;

  public CommunicationSatelliteParam(SatelliteType aType, String aName, double aBatteryLevel,
      double aBandwidth) {
    super(aType, aName, aBatteryLevel);
    bandwidth = aBandwidth;
  }
}
