package constellation.Model.Factory.SatelliteFactory;

import constellation.Model.Domain.Satellite.CommunicationSatellite;
import constellation.Model.Domain.Satellite.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.CommunicationSatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteType;
import org.springframework.stereotype.Component;

@Component
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
