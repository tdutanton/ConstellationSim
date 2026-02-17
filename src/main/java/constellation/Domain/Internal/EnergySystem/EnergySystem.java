package constellation.Domain.Internal.EnergySystem;

import java.util.Random;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Класс, моделирующий систему энергоснабжения спутника.
 * <p>
 * Управляет уровнем заряда батареи, который инициализируется случайным значением в диапазоне от 0.0
 * до 1.0 (где 1.0 означает 100% заряда). Поддерживает операцию потребления энергии с защитой от
 * отрицательных значений и автоматическим ограничением минимального уровня заряда на нуле.
 * </p>
 */
@Getter
@EqualsAndHashCode
public class EnergySystem {

  /**
   * Текущий уровень заряда батареи (в долях от 1, где 1.0 = 100%).
   */
  protected double batteryLevel;

  protected EnergySystem() {batteryLevel = 0.0;}

  /**
   * Потребляет указанное количество энергии из батареи.
   * <p>
   * Уровень заряда уменьшается на величину {@code amount}, но не может опуститься ниже 0.0.
   * </p>
   *
   * @param amount объём потребляемой энергии (в долях от полного заряда); должен быть
   *               неотрицательным
   * @throws IllegalArgumentException если {@code amount} меньше нуля
   */
  public void consume(double amount) {
    if (amount < 0.0) {
      throw new IllegalArgumentException(
          "Значение для снижения заряда батареи не должно быть отрицательным");
    }
    batteryLevel = Math.max(0.0, batteryLevel - amount);
  }
}