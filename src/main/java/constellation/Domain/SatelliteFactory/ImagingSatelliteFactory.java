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
    if (param instanceof ImagingSatelliteParam) {
      String name = param.getName();
      double batteryLevel = param.getBatteryLevel();
      double resolution = ((ImagingSatelliteParam) param).getResolution();
      return new ImagingSatellite(name, batteryLevel, resolution);
    } else {
      throw new SpaceOperationException("Тип спутника не соответствует типу Imaging");
    }
  }

  @Override
  public boolean isSatelliteTypeSupported(SatelliteType type) {
    return type == SatelliteType.IMAGE;
  }
}
