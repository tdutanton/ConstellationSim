package constellation.Service.ConstellationService.ServiceDB;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Repository.ConstellationsRepository;
import constellation.Repository.SatellitesRepository;
import java.util.ArrayList;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

  @CacheEvict(value = "constellations", key = "'all'")
  @Transactional
  public void createAndSaveConstellation(String name) {
    if (repository.findByConstellationName(name).isPresent()) {
      System.out.println("Такая группировка уже есть");
      return;
    }
    if (isConstellationCorrect(name)) {
      SatelliteConstellation constellation = new SatelliteConstellation.ConstellationBuilder().setConstellationName(
          name).build();
      repository.save(constellation);
    }
  }

  @Transactional
  @Caching(evict = {
      @CacheEvict(value = "constellation", key = "#constellationName"),
      @CacheEvict(value = "constellations", key = "'all'"),
      @CacheEvict(value = "satellites", key = "'all'")
  })
  public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
    SatelliteConstellation constellation = repository.findByConstellationName(
            constellationName)
        .orElseThrow(
            () -> new IllegalArgumentException("Группировка не найдена: " + constellationName));
    satellite.setConstellation(constellation);
    constellation.getSatellites().add(satellite);
    satellitesRepository.save(satellite);
  }

  @Transactional
  public void executeConstellationMission(String constellationName) {
    SatelliteConstellation constellation = repository.findByConstellationName(
            constellationName)
        .orElseThrow(
            () -> new IllegalArgumentException("Группировка не найдена: " + constellationName));
    if (constellation != null) {
      System.out.printf("=== ВЫПОЛНЕНИЕ МИССИЙ ДЛЯ ГРУППИРОВКИ: %s ===%n", constellationName);
      constellation.executeAllMissions();
    }
  }

  @Transactional
  public void activateAllSatellites(String constellationName) {
    SatelliteConstellation constellation = repository.findByConstellationName(
            constellationName)
        .orElseThrow(
            () -> new IllegalArgumentException("Группировка не найдена: " + constellationName));
    if (constellation != null) {
      System.out.printf("=== АКТИВАЦИЯ СПУТНИКОВ В ГРУППИРОВКЕ: %s ===%n", constellationName);
      constellation.activateSatellites();
    }
  }

  @Transactional
  public void showConstellationStatus(String constellationName) {
    SatelliteConstellation constellation = repository.findByConstellationName(
            constellationName)
        .orElseThrow(
            () -> new IllegalArgumentException("Группировка не найдена: " + constellationName));
    if (constellation != null) {
      System.out.printf("=== СТАТУС ГРУППИРОВКИ: %s ===%n", constellationName);
      System.out.printf("Количество спутников: %d%n", constellation.getSatellites().size());
      System.out.println(constellation.getSatellitesStatus());
      System.out.println(constellation.getSatellites());
    }
  }

  @Transactional
  @Caching(evict = {
      @CacheEvict(value = "constellation", key = "#constellationName"),
      @CacheEvict(value = "constellations", key = "'all'"),
      @CacheEvict(value = "satellites", key = "'all'")
  })
  public boolean deleteSatellite(String constellationName, String satelliteName) {
    SatelliteConstellation constellation = repository.findByConstellationName(constellationName)
        .orElseThrow(
            () -> new IllegalArgumentException("Группировка не найдена: " + constellationName));
    Satellite satellite = constellation.satelliteByName(satelliteName);
    if (satellite == null) {
      return false;
    }
    constellation.deleteSatellite(satellite);
    if (constellation.getSatellites().isEmpty()) {
      repository.delete(constellation);
    }
    return true;
  }

  private SatelliteConstellation copyForCache(SatelliteConstellation original) {
    SatelliteConstellation copy = new SatelliteConstellation();
    copy.setId(original.getId());
    copy.setConstellationName(original.getConstellationName());
    copy.setCreatedAt(original.getCreatedAt());
    copy.setUpdatedAt(original.getUpdatedAt());
    copy.setSatellites(new ArrayList<>(original.getSatellites()));
    return copy;
  }

  @Cacheable(value = "constellation", key = "#constellationName", unless = "#result == null")
  @Transactional
  public SatelliteConstellation constellationFromRepository(String constellationName) {
    SatelliteConstellation c = repository.findByConstellationName(constellationName).orElse(null);
    if (c != null) {
      c.getSatellites().size();
      return copyForCache(c);
    }
    return null;
  }

  @Transactional
  public Satellite satelliteByName(String constellationName, String name) {
    SatelliteConstellation constellation = repository.findByConstellationName(
            constellationName)
        .orElseThrow(
            () -> new IllegalArgumentException("Группировка не найдена: " + constellationName));
    Satellite satellite = satellitesRepository.findByName(name).orElseThrow(
        () -> new IllegalArgumentException("Спутник не найден: " + name));
    if (Objects.equals(satellite.getConstellation().getId(), constellation.getId())) {
      return satellite;
    } else {
      return null;
    }
  }

  @Cacheable(value = "constellations", key = "'all'")
  @Transactional
  public ArrayList<SatelliteConstellation> constellations() {
    ArrayList<SatelliteConstellation> result =
        (ArrayList<SatelliteConstellation>) repository.findAll();
    ArrayList<SatelliteConstellation> cacheCopy = new ArrayList<>();
    for (SatelliteConstellation c : result) {
      c.getSatellites().size();
      cacheCopy.add(copyForCache(c));
    }
    return cacheCopy;
  }

}
