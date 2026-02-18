package constellation.Domain.SatelliteFactory;

import constellation.Domain.Satellite.Satellite;

public interface SatelliteFactory {

  Satellite createSatellite(String name, double batteryLevel);

  Satellite createSatelliteWithParameter(String name, double batteryLevel, double parameter);
}
