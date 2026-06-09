package constellation.Service.SatelliteService.Impl;

import constellation.Model.Domain.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Factory.SatelliteFactory.SatelliteFactory;
import constellation.Repository.SatellitesRepository;
import constellation.Service.SatelliteService.SatelliteService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SatelliteServiceDB implements SatelliteService {

  private final SatellitesRepository repository;
  private final List<SatelliteFactory> factories;

  @Transactional
  @CacheEvict(value = "satellites", key = "'all'")
  @Override
  public Satellite createSatellite(SatelliteParam param) throws SpaceOperationException {
    for (SatelliteFactory factory : factories) {
      if (factory.isSatelliteTypeSupported(param.getType())) {
        try {
          return factory.createSatelliteWithParameter(param);
        } catch (SpaceOperationException e) {
          System.out.printf("Ошибка создания спутника: %s%n", e);
        }
      }
    }
    throw new SpaceOperationException(
        String.format("Тип спутника %s не поддерживается сервисом",
            param.getType()));
  }

  @Cacheable(value = "satellite", key = "#id")
  @Override
  public Optional<Satellite> findById(Long id) {
    return repository.findById(id);
  }

  @Cacheable(value = "satellites", key = "'all'")
  @Override
  public List<Satellite> findAll() {
    return repository.findAll();
  }

  @Transactional
  @Caching(evict = {
      @CacheEvict(value = "satellite", key = "#id"),
      @CacheEvict(value = "satellites", key = "'all'")
  })
  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  @Cacheable(value = "satellite", key = "#constellationName + '::' + #satelliteName")
  public Optional<Satellite> findByName(String constellationName, String satelliteName) {
    return repository.findByName(satelliteName);
  }

  @Transactional
  @Override
  public void executeMission(Satellite satellite) {
    Optional<Satellite> resultOpt = repository.findByName(satellite.getName());
    resultOpt.ifPresent(Satellite::executeMission);
  }
}
