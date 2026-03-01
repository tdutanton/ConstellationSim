package constellation.Model.Factory.SatelliteFactory;

import constellation.Model.Domain.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;

public interface SatelliteFactory {

  boolean isSatelliteTypeSupported(SatelliteType type);

  Satellite createSatelliteWithParameter(SatelliteParam param) throws SpaceOperationException;
}
