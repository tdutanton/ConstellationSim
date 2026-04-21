package constellation.Service.SatelliteService.Impl;

import constellation.Model.Domain.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Factory.SatelliteFactory.SatelliteFactory;
import constellation.Repository.DBRepository.SatellitesRepository;
import constellation.Service.SatelliteService.SatelliteService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SatelliteServiceDB implements SatelliteService {

  private final SatellitesRepository repository;
  private final List<SatelliteFactory> factories;

  @Transactional
  @Override
  public Satellite createSatellite(SatelliteParam param) throws SpaceOperationException {
    for (SatelliteFactory factory : factories) {
      if (factory.isSatelliteTypeSupported(param.getType())) {
        try {
          Satellite result = factory.createSatelliteWithParameter(param);
          return repository.save(result);
        } catch (SpaceOperationException e) {
          System.out.printf("Ошибка создания спутника: %s%n", e);
        }
      }
    }
    throw new SpaceOperationException(
        String.format("Тип спутника %s не поддерживается сервисом",
            param.getType()));
  }


  @Transactional
  @Override
  public void executeMission(Satellite satellite) {
    Optional<Satellite> resultOpt = repository.findBySatelliteName(satellite.getName());
    resultOpt.ifPresent(Satellite::executeMission);
  }
}
