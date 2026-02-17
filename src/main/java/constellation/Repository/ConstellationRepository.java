package constellation.Repository;

import constellation.Domain.Constellation.SatelliteConstellation;
import constellation.Domain.Satellite.Satellite;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * Репозиторий для хранения и управления спутниковыми группировками.
 * <p>
 * Представляет собой Spring-компонент, обеспечивающий централизованное хранилище объектов типа
 * {@link SatelliteConstellation} по их названиям. Поддерживает безопасное добавление группировок и
 * спутников с проверкой на дублирование и существование целевой группировки.
 * </p>
 */
@Component
public class ConstellationRepository {

  private final Map<String, SatelliteConstellation> constellations;

  /**
   * Создаёт пустой репозиторий спутниковых группировок.
   */
  public ConstellationRepository() {
    this.constellations = new HashMap<>();
  }

  /**
   * Добавляет новую спутниковую группировку в репозиторий.
   * <p>
   * Если группировка с таким названием уже существует, добавление не производится, и выводится
   * соответствующее сообщение в консоль.
   * </p>
   *
   * @param constellation группировка для добавления; не должна быть {@code null}
   */
  public void addConstellation(SatelliteConstellation constellation) {
    safetyAddConstellation(constellation);
  }

  /**
   * Удаляет спутниковую группировку из репозитория.
   *
   * @param constellation группировка для удаления; не должна быть {@code null}
   */
  public void deleteConstellation(SatelliteConstellation constellation) {
    safetyDeleteConstellation(constellation);
  }

  /**
   * Добавляет спутник в существующую спутниковую группировку по её названию.
   * <p>
   * Если указанной группировки нет в репозитории, операция игнорируется, и выводится сообщение об
   * ошибке.
   * </p>
   *
   * @param constellationName название группировки, в которую нужно добавить спутник
   * @param satellite         спутник для добавления; не должен быть {@code null}
   */
  public void addSatellite(String constellationName, Satellite satellite) {
    safetyAddSatellite(constellationName, satellite);
  }

  /**
   * Удаляет спутник из существующей спутниковой группировки по её названию.
   * <p>
   * Если указанной группировки нет в репозитории, операция игнорируется, и выводится сообщение об
   * ошибке.
   * </p>
   *
   * @param constellationName название группировки
   * @param satellite         спутник для удаления; не должен быть {@code null}
   */
  public void deleteSatellite(String constellationName, Satellite satellite) {
    safetyDeleteSatellite(constellationName, satellite);
  }

  /**
   * Проверяет, содержится ли указанная группировка в репозитории по её названию.
   *
   * @param constellation группировка для проверки
   * @return {@code true}, если группировка уже существует в репозитории, иначе {@code false}
   */
  private boolean isInRepository(SatelliteConstellation constellation) {
    return constellations.containsKey(constellation.getConstellationName());
  }

  /**
   * Проверяет, содержится ли группировка с заданным названием в репозитории.
   *
   * @param constellationName название группировки
   * @return {@code true}, если группировка с таким именем существует, иначе {@code false}
   */
  private boolean isInRepository(String constellationName) {
    return constellations.containsKey(constellationName);
  }

  /**
   * Безопасно добавляет спутниковую группировку в хранилище, избегая дублирования.
   *
   * @param constellation группировка для добавления
   */
  private void safetyAddConstellation(SatelliteConstellation constellation) {
    if (!isInRepository(constellation)) {
      constellations.put(constellation.getConstellationName(), constellation);
      System.out.println("Сохранена группировка: " + constellation.getConstellationName());
      return;
    }
    System.out.printf("Группировка %s уже существует в хранилище.%n",
        constellation.getConstellationName());
  }

  /**
   * Безопасно удаляет спутниковую группировку.
   *
   * @param constellation группировка для удаления
   */
  private void safetyDeleteConstellation(SatelliteConstellation constellation) {
    if (isInRepository(constellation)) {
      constellations.remove(constellation.getConstellationName());
      System.out.println("Удалена группировка: " + constellation.getConstellationName());
      return;
    }
    System.out.printf("Группировка %s не существует в хранилище.%n",
        constellation.getConstellationName());
  }

  /**
   * Возвращает спутниковую группировку по её названию.
   * <p>
   * Метод является защищённым и предназначен для использования внутри пакета или в подклассах. Если
   * название группировки {@code null}, выбрасывается {@link NullPointerException}.
   * </p>
   *
   * @param constellationName название искомой группировки
   * @return объект {@link SatelliteConstellation}, если он найден
   * @throws NullPointerException если {@code constellationName} равен {@code null}
   */
  public SatelliteConstellation constellationByName(String constellationName) {
    Objects.requireNonNull(constellationName, "Группировки" + constellationName + "не существует");
    return constellations.get(constellationName);
  }

  /**
   * Безопасно добавляет спутник в указанную группировку, предварительно проверяя её наличие.
   *
   * @param constellationName название группировки
   * @param satellite         добавляемый спутник
   */
  private void safetyAddSatellite(String constellationName, Satellite satellite) {
    if (isInRepository(constellationName)) {
      SatelliteConstellation constellation = constellationByName(constellationName);
      constellation.addSatellite(satellite);
      System.out.printf("Добавлен спутник %s в группировку %s%n", satellite.getName(),
          constellation.getConstellationName());
      return;
    }
    System.out.println("Группировки " + constellationName + " не существует"); // добавлены пробелы
  }

  /**
   * Безопасно удаляет спутник из указанной группировки, предварительно проверяя её наличие.
   *
   * @param constellationName название группировки
   * @param satellite         удаляемый спутник
   */
  private void safetyDeleteSatellite(String constellationName, Satellite satellite) {
    if (isInRepository(constellationName)) { // ← проверка по имени
      SatelliteConstellation constellation = constellationByName(constellationName);
      constellation.deleteSatellite(satellite);
      System.out.printf("Удалён спутник %s из группировки %s%n", satellite.getName(),
          constellation.getConstellationName()); // исправлено сообщение
      return;
    }
    System.out.println("Группировки " + constellationName + " не существует");
  }

  public boolean containsConstellations() {
    return !constellations.isEmpty();
  }
}