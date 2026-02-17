package constellation.Domain.SatelliteFactory;

import constellation.Domain.Satellite.CommunicationSatellite;
import constellation.Domain.Satellite.Satellite;

public class CommunicationSatelliteFactory implements SatelliteFactory {
  @Override
  public Satellite createSatellite(String name, double batteryLevel) {
    return new CommunicationSatellite(name, batteryLevel);
  }

  @Override
  public Satellite createSatelliteWithParameter(String name, double batteryLevel, double parameter) {
    return new CommunicationSatellite(name, batteryLevel, parameter);
  }
}
