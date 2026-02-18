package constellation.Service;

import constellation.Domain.Constellation.SatelliteConstellation;
import constellation.Domain.Satellite.Satellite;
import constellation.Repository.ConstellationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис центра управления космическими операциями.
 * <p>
 * Обеспечивает высокоуровневое взаимодействие с репозиторием спутниковых группировок: создание,
 * активацию, выполнение миссий и отображение статуса группировок. Все операции включают базовую
 * валидацию входных данных и вывод информационных сообщений в консоль при выполнении действий.
 * </p>
 */
@Service
@AllArgsConstructor
public class SpaceOperationCenterService {

  private final ConstellationRepository repository;

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

  /**
   * Создаёт новую спутниковую группировку с заданным названием и сохраняет её в репозиторий.
   * <p>
   * Если название некорректно (null или пустое), операция игнорируется.
   * </p>
   *
   * @param name название создаваемой группировки
   */
  public void createAndSaveConstellation(String name) {
    if (isConstellationCorrect(name)) {
      SatelliteConstellation constellation = new SatelliteConstellation.ConstellationBuilder().setConstellationName(
          name).build();
      repository.addConstellation(constellation);
    }
  }

  /**
   * Добавляет спутник в существующую спутниковую группировку по её названию.
   * <p>
   * Валидация спутника и названия группировки выполняется внутри репозитория. Если группировка не
   * существует, операция завершится без эффекта.
   * </p>
   *
   * @param constellationName название целевой группировки
   * @param satellite         добавляемый спутник
   */
  public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
    repository.addSatellite(constellationName, satellite);
  }

  /**
   * Запускает выполнение миссий для всех спутников указанной группировки.
   * <p>
   * Перед выполнением проверяется существование группировки. При успешном запуске выводится
   * заголовок в консоль.
   * </p>
   *
   * @param constellationName название группировки
   */
  public void executeConstellationMission(String constellationName) {
    SatelliteConstellation constellation = constellationFromRepository(constellationName);
    if (constellation != null) {
      System.out.printf("=== ВЫПОЛНЕНИЕ МИССИЙ ДЛЯ ГРУППИРОВКИ: %s ===%n", constellationName);
      constellation.executeAllMissions();
    }
  }

  /**
   * Активирует все спутники в указанной группировке.
   * <p>
   * Перед активацией проверяется существование группировки. При успешной активации выводится
   * заголовок в консоль.
   * </p>
   *
   * @param constellationName название группировки
   */
  public void activateAllSatellites(String constellationName) {
    SatelliteConstellation constellation = constellationFromRepository(constellationName);
    if (constellation != null) {
      System.out.printf("=== АКТИВАЦИЯ СПУТНИКОВ В ГРУППИРОВКЕ: %s ===%n", constellationName);
      constellation.activateSatellites();
    }
  }

  /**
   * Извлекает спутниковую группировку из репозитория по её названию с предварительной валидацией.
   *
   * @param constellationName название группировки
   * @return объект группировки, если она существует и название валидно; иначе {@code null}
   */
  private SatelliteConstellation constellationFromRepository(String constellationName) {
    if (isConstellationCorrect(constellationName)) {
      return repository.constellationByName(constellationName);
    }
    return null;
  }

  /**
   * Отображает текущий статус указанной спутниковой группировки.
   * <p>
   * Вывод в консоль включает:
   * <ul>
   *   <li>название группировки,</li>
   *   <li>количество спутников,</li>
   *   <li>список их состояний,</li>
   *   <li>строковое представление самих спутников.</li>
   * </ul>
   * Если группировка не найдена, вывод не производится.
   * </p>
   *
   * @param constellationName название группировки
   */
  public void showConstellationStatus(String constellationName) {
    SatelliteConstellation constellation = constellationFromRepository(constellationName);
    if (constellation != null) {
      System.out.printf("=== СТАТУС ГРУППИРОВКИ: %s ===%n", constellationName);
      System.out.printf("Количество спутников: %d%n", constellation.getSatellites().size());
      System.out.println(constellation.getSatellitesStatus());
      System.out.println(constellation.getSatellites());
    }
  }
}