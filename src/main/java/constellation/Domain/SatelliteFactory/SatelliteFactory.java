package constellation.Domain.SatelliteFactory;

import constellation.Domain.Satellite.Exception.SpaceOperationException;
import constellation.Domain.Satellite.Satellite;
import constellation.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Domain.Satellite.SatelliteType;

public interface SatelliteFactory {

  boolean isSatelliteTypeSupported(SatelliteType type);

  Satellite createSatelliteWithParameter(SatelliteParam param) throws SpaceOperationException;
}
