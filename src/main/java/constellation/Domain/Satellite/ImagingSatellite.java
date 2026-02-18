package constellation.Domain.Satellite;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Спутник дистанционного зондирования Земли (ДЗЗ). Выполняет миссию по съёмке территории с заданным
 * пространственным разрешением. Каждая миссия потребляет заряд батареи и увеличивает счётчик
 * сделанных снимков.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class ImagingSatellite extends Satellite {

  /**
   * Пространственное разрешение съёмки в метрах на пиксель.
   */
  private final double resolution;

  /**
   * Количество сделанных снимков за время работы спутника.
   */
  private int photosTaken;

  /**
   * Префикс имени по умолчанию для спутников данного типа.
   */
  private static final String DEFAULT_NAME = "ДЗЗ";

  /**
   * Последовательный номер для генерации уникальных имён спутников.
   */
  private static int SERIAL_NUMBER = 1;

  /**
   * Уровень потребления заряда батареи за одну миссию (8%).
   */
  private static final double BATTERY_PER_MISSION = 0.08;

  /**
   * Конструирует новый спутник ДЗЗ с заданным разрешением. Автоматически генерирует уникальное имя
   * на основе префикса {@link #DEFAULT_NAME} и внутреннего счётчика {@link #SERIAL_NUMBER}.
   * Инициализирует счётчик снимков значением 0.
   *
   * @param aResolution пространственное разрешение съёмки в метрах на пиксель (должно быть ≥ 0)
   * @throws IllegalArgumentException если разрешение отрицательное
   */
  public ImagingSatellite(double aResolution) {
    super(DEFAULT_NAME, SERIAL_NUMBER);
    if (aResolution < 0.0) {
      throw new IllegalArgumentException("Разрешение не должно быть отрицательным");
    }
    resolution = aResolution;
    photosTaken = 0;
    SERIAL_NUMBER++;
  }

  public ImagingSatellite(String name, double batteryLevel) {
    super(name, SERIAL_NUMBER, batteryLevel);
    resolution = 0.0;
    photosTaken = 0;
    SERIAL_NUMBER++;
  }

  public ImagingSatellite(String name, double batteryLevel, double aResolution) {
    super(name, SERIAL_NUMBER, batteryLevel);
    if (aResolution < 0.0) {
      throw new IllegalArgumentException("Разрешение не должно быть отрицательным");
    }
    resolution = aResolution;
    photosTaken = 0;
    SERIAL_NUMBER++;
  }

  /**
   * Возвращает строковое представление спутника для отладки. Формат:
   * {@code ImagingSatellite{resolution=..., photosTaken=..., name='...', isActive=...,
   * batteryLevel=...}}
   *
   * @return строка с ключевыми параметрами спутника
   */
  @Override
  public String toString() {
    return String.format(
        "%s{resolution=%.1f, photosTaken=%d, name='%s' isActive=%s, batteryLevel=%.2f}",
        this.getClass().getSimpleName(),
        resolution,
        photosTaken,
        name,
        state.isActive(),
        energy.getBatteryLevel()
    );
  }

  /**
   * Выполняет миссию по съёмке территории. Если спутник активен, он:
   * <ul>
   *   <li>выводит сообщение о начале съёмки (при включённом {@code consolePrintMode});</li>
   *   <li>расходует заряд батареи ({@link #BATTERY_PER_MISSION});</li>
   *   <li>проверяет, не требуется ли деактивация из-за низкого заряда;</li>
   *   <li>увеличивает счётчик снимков.</li>
   * </ul>
   * Если спутник не активен — выводится сообщение об ошибке.
   */
  @Override
  public void performMission() {
    if (state.isActive()) {
      System.out.printf("✅ %s: Съемка территории с разрешением %.1f м/пиксель%n", name, resolution);
      energy.consume(BATTERY_PER_MISSION);
      handleChangeBatteryLevel();
      takePhoto();
    } else {
      System.out.printf("⛔ %s: Не может выполнить съемку - не активен%n", name);
    }
  }

  /**
   * Делает один снимок. Увеличивает внутренний счётчик {@link #photosTaken} на единицу. При
   * включённом {@code consolePrintMode} выводит подтверждающее сообщение.
   */
  private void takePhoto() {
    photosTaken++;
    if (consolePrintMode) {
      System.out.printf("✅ %s : Снимок #%d сделан!%n", name, photosTaken);
    }
  }
}