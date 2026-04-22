package constellation.Service.ConstellationService.ServiceDB;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Repository.DBRepository.ConstellationsRepository;
import constellation.Repository.DBRepository.SatellitesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("dbConstellationService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConstellationService {

  private final ConstellationsRepository repository;
  private final SatellitesRepository satellitesRepository;

  /**
   * Проверяет корректность объекта группировки: он не должен быть {@code null}, а его название —
   * ненулевым и не пустым/не состоящим только из пробелов.
   *
   * @param constellation проверяемая группировка
   * @return {@code true}, если объект валиден, иначе {@code false}
   */
  private boolean isConstellationCorrect(SatelliteConstellation constellation) {
    if (constellation == null) {
      return false;
    }
    return (constellation.getConstellationName() != null) && !constellation.getConstellationName()
        .isBlank();
  }

  /**
   * Проверяет корректность названия группировки: оно не должно быть {@code null} и не должно быть
   * пустой или содержащей только пробелы строкой.
   *
   * @param constellationName название группировки
   * @return {@code true}, если название валидно, иначе {@code false}
   */
  private boolean isConstellationCorrect(String constellationName) {
    if (constellationName == null) {
      return false;
    }
    return !constellationName.isBlank();
  }

  /**
   * Проверяет корректность спутника: он не должен быть {@code null}, а его имя — ненулевым и не
   * пустым/не состоящим только из пробелов.
   *
   * @param satellite проверяемый спутник
   * @return {@code true}, если спутник валиден, иначе {@code false}
   */
  private boolean isSatelliteCorrect(Satellite satellite) {
    if (satellite == null) {
      return false;
    }
    return (satellite.getName() != null) && !satellite.getName().isBlank();
  }

  @Transactional
  public void createAndSaveConstellation(String name) {
    if (isConstellationCorrect(name)) {
      SatelliteConstellation constellation = new SatelliteConstellation.ConstellationBuilder().setConstellationName(
          name).build();
      repository.save(constellation);
    }
  }

  @Transactional
  public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
    SatelliteConstellation constellation = repository.findByConstellationName(
            constellationName)
        .orElseThrow(
            () -> new IllegalArgumentException("Группировка не найдена: " + constellationName));
    satellite.setConstellation(constellation);
    constellation.getSatellites().add(satellite);
    satellitesRepository.save(satellite);
  }

}
