package constellation.Domain.Internal;

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

  private static double MAX_BATTERY_LEVEL = 100.0;
  private static double MIN_BATTERY_LEVEL = 0.0;

  /**
   * Текущий уровень заряда батареи (в долях от 1, где 1.0 = 100%).
   */
  private double batteryLevel;

  /**
   * Экземпляр генератора случайных чисел, используемый для инициализации заряда.
   */
  private static final Random RANDOM = new Random();

  /**
   * Генерирует случайный начальный уровень заряда батареи в диапазоне [0.0, 1.0).
   *
   * @return случайное значение уровня заряда
   */
  private double generateBatteryLevel() {
    return RANDOM.nextDouble();
  }

  /**
   * Конструктор системы энергоснабжения.
   * <p>
   * Инициализирует уровень заряда случайным значением от 0.0 до 1.0.
   * </p>
   */
  public EnergySystem() {
    batteryLevel = generateBatteryLevel();
  }

  /**
   * Конструктор системы энергоснабжения с параметром
   * @param batteryLevel уровень заряда батареи в процентах. Не может быть больше 100 % или меньше 0 %
   */
  public EnergySystem(double batteryLevel) {
    if (batteryLevel > MAX_BATTERY_LEVEL || batteryLevel < MIN_BATTERY_LEVEL) {
      throw new IllegalArgumentException(
          String.format("Заряд батареи не может быть больше %.2f и меньше %.2f",
              MAX_BATTERY_LEVEL, MIN_BATTERY_LEVEL));
    }
    this.batteryLevel = batteryLevel / 100.0;
  }

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