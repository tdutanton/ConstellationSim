package constellation.Service.SatelliteService;

import constellation.Model.Domain.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;

public interface SatelliteService {

  Satellite createSatellite(SatelliteParam param) throws SpaceOperationException;

  void executeMission(Satellite satellite);
}
