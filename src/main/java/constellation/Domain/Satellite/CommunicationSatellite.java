package constellation.Domain.Satellite;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Спутник связи. Предназначен для передачи данных с заданной пропускной способностью. Каждая миссия
 * по передаче данных потребляет заряд батареи и сопровождается логгированием объёма переданной
 * информации (при включённом режиме вывода).
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class CommunicationSatellite extends Satellite {

  /**
   * Пропускная способность канала связи в мегабитах в секунду (Мбит/с).
   */
  private final double bandwidth;

  /**
   * Префикс имени по умолчанию для спутников данного типа.
   */
  private static final String DEFAULT_NAME = "Связь";

  /**
   * Последовательный номер для генерации уникальных имён спутников.
   */
  private static int SERIAL_NUMBER = 1;

  /**
   * Уровень потребления заряда батареи за одну миссию (5%).
   */
  private static final double BATTERY_PER_MISSION = 0.05;

  /**
   * Конструирует новый спутник связи с заданной пропускной способностью. Автоматически генерирует
   * уникальное имя на основе префикса {@link #DEFAULT_NAME} и внутреннего счётчика
   * {@link #SERIAL_NUMBER}.
   *
   * @param aBandwidth пропускная способность в Мбит/с (должна быть ≥ 0)
   * @throws IllegalArgumentException если пропускная способность отрицательна
   */
  public CommunicationSatellite(double aBandwidth) {
    super(DEFAULT_NAME, SERIAL_NUMBER);
    if (aBandwidth < 0.0) {
      throw new IllegalArgumentException("Пропускная способность не должна быть отрицательной");
    }

    bandwidth = aBandwidth;
    SERIAL_NUMBER++;
  }

  /**
   * Возвращает строковое представление спутника для отладки. Формат:
   * {@code CommunicationSatellite{bandwidth=..., name='...', isActive=..., batteryLevel=...}}
   *
   * @return строка с ключевыми параметрами спутника
   */
  @Override
  public String toString() {
    return String.format(
        "%s{bandwidth=%s, name='%s', isActive=%s, batteryLevel=%.2f}",
        this.getClass().getSimpleName(),
        bandwidth,
        name,
        state.isActive(),
        energy.getBatteryLevel()
    );
  }

  /**
   * Выполняет миссию по передаче данных. Если спутник активен, он:
   * <ul>
   *   <li>выводит сообщение о начале передачи (при включённом {@code consolePrintMode});</li>
   *   <li>эмулирует отправку данных объёмом, равным пропускной способности;</li>
   *   <li>расходует заряд батареи ({@link #BATTERY_PER_MISSION});</li>
   *   <li>проверяет, не требуется ли деактивация из-за низкого заряда.</li>
   * </ul>
   * Если спутник не активен — выводится сообщение об ошибке.
   */
  @Override
  public void performMission() {
    if (state.isActive()) {
      System.out.printf("✅ %s: Передача данных со скоростью %.1f Мбит/с%n", name, bandwidth);
      sendData(bandwidth);
      energy.consume(BATTERY_PER_MISSION);
      handleChangeBatteryLevel();
    } else {
      System.out.printf("⛔ %s не может передать данные - не активен%n", name);
    }
  }

  /**
   * Эмулирует отправку данных. При включённом {@code consolePrintMode} выводит сообщение об
   * успешной передаче с указанием объёма данных в мегабитах.
   *
   * @param data объём передаваемых данных в мегабитах
   */
  private void sendData(double data) {
    if (consolePrintMode) {
      System.out.printf("✅ %s отправил %.0f Мбит данных!%n", name, data);
    }
  }
}