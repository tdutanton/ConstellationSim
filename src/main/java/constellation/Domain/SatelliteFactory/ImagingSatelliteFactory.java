package constellation.Domain.SatelliteFactory;

import constellation.Domain.Satellite.Exception.SpaceOperationException;
import constellation.Domain.Satellite.ImagingSatellite;
import constellation.Domain.Satellite.Satellite;
import constellation.Domain.Satellite.SatelliteParam.ImagingSatelliteParam;
import constellation.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Domain.Satellite.SatelliteType;

public class ImagingSatelliteFactory implements SatelliteFactory {

  @Override
  public Satellite createSatelliteWithParameter(SatelliteParam param)
      throws SpaceOperationException {
    if (param instanceof ImagingSatelliteParam imgparam) {
      String name = imgparam.getName();
      double batteryLevel = imgparam.getBatteryLevel();
      double resolution = imgparam.getResolution();
      return new ImagingSatellite(name, batteryLevel, resolution);
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
