package constellation.Domain.SatelliteFactory;

import constellation.Domain.Satellite.CommunicationSatellite;
import constellation.Domain.Satellite.Exception.SpaceOperationException;
import constellation.Domain.Satellite.Satellite;
import constellation.Domain.Satellite.SatelliteParam.CommunicationSatelliteParam;
import constellation.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Domain.Satellite.SatelliteType;

public class CommunicationSatelliteFactory implements SatelliteFactory {

  @Override
  public Satellite createSatelliteWithParameter(SatelliteParam param)
      throws SpaceOperationException {
    if (param instanceof CommunicationSatelliteParam commParam) {
      String name = commParam.getName();
      double batteryLevel = commParam.getBatteryLevel();
      double bandwidth = commParam.getBandwidth();
      return new CommunicationSatellite(name, batteryLevel, bandwidth);
    } else {
      throw new SpaceOperationException(
          String.format("Ожидался CommunicationSatelliteParam, получен: %s",
              param.getClass().getSimpleName())
      );
    }
  }

  @Override
  public boolean isSatelliteTypeSupported(SatelliteType type) {
    return type == SatelliteType.COMMUNICATION;
  }
}
