package constellation.Model.Factory.SatelliteFactory;

import constellation.Model.Domain.Satellite.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.ImagingSatellite;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.ImagingSatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteType;
import org.springframework.stereotype.Component;

@Component
public class ImagingSatelliteFactory implements SatelliteFactory {

  @Override
  public Satellite createSatelliteWithParameter(SatelliteParam param)
      throws SpaceOperationException {
    if (param instanceof ImagingSatelliteParam imgParam) {
      String name = imgParam.getName();
      double batteryLevel = imgParam.getBatteryLevel();
      double resolution = imgParam.getResolution();
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
