package constellation.Service.Satellite;

import constellation.Model.Domain.Satellite.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;

public interface SatelliteService {

  Satellite createSatellite(SatelliteParam param) throws SpaceOperationException;
}
