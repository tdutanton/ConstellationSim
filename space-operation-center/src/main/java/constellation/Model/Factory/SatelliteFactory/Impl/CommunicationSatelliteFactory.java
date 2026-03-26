package constellation.Model.Factory.SatelliteFactory.Impl;

import constellation.Model.Domain.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.CommunicationSatellite;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.CommunicationSatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import constellation.Model.Factory.SatelliteFactory.SatelliteFactory;
import org.springframework.stereotype.Component;

@Component
public class CommunicationSatelliteFactory implements SatelliteFactory {

  @Override
  public Satellite createSatelliteWithParameter(SatelliteParam param)
      throws SpaceOperationException {
    if (param instanceof CommunicationSatelliteParam commParam) {
      return new CommunicationSatellite(commParam.getName(), commParam.getBatteryLevel(),
          commParam.getBandwidth());
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
