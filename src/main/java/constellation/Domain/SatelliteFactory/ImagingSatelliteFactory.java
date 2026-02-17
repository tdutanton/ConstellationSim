package constellation.Domain.SatelliteFactory;

import constellation.Domain.Satellite.ImagingSatellite;
import constellation.Domain.Satellite.Satellite;

public class ImagingSatelliteFactory implements SatelliteFactory {
  @Override
  public Satellite createSatellite(String name, double batteryLevel) {
    return new ImagingSatellite(name, batteryLevel);
  }

  @Override
  public Satellite createSatelliteWithParameter(String name, double batteryLevel, double resolution) {
    return new ImagingSatellite(name, batteryLevel, resolution);
  }
}
