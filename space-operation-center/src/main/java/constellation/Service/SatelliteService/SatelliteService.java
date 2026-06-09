package constellation.Service.SatelliteService;

import constellation.Model.Domain.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import java.util.List;
import java.util.Optional;

public interface SatelliteService {

  Satellite createSatellite(SatelliteParam param) throws SpaceOperationException;

  Optional<Satellite> findById(Long id);

  List<Satellite> findAll();

  void deleteById(Long id);

  Optional<Satellite> findByName(String constellationName, String satelliteName);

  void executeMission(Satellite satellite);
}
