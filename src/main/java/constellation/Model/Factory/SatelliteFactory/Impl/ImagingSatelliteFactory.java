package constellation.Model.Factory.SatelliteFactory.Impl;

import constellation.Model.Domain.Internal.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.ImagingSatellite;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.ImagingSatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import constellation.Model.Factory.SatelliteFactory.SatelliteFactory;
import org.springframework.stereotype.Component;

@Component
public class ImagingSatelliteFactory implements SatelliteFactory {

  @Override
  public Satellite createSatelliteWithParameter(SatelliteParam param)
      throws SpaceOperationException {
    if (param instanceof ImagingSatelliteParam imgParam) {
      return new ImagingSatellite(imgParam.getName(), imgParam.getBatteryLevel(),
          imgParam.getResolution());
    } else {
      throw new SpaceOperationException(
          String.format("Ожидался ImagingSatelliteParam, получен: %s",
              param.getClass().getSimpleName())
      );
    }
  }

  @Override
  public boolean isSatelliteTypeSupported(SatelliteType type) {
    return type == SatelliteType.IMAGE;
  }
}
