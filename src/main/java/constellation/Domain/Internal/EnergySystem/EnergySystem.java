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
  @Getter
  private double batteryLevel;

  private EnergySystem(EnergySystemBuilder builder) {
    batteryLevel = builder.batteryLevel;
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

  /**
   * Внутренний статичный класс для создания экземпляров EnergySystem при помощи паттерна Строитель
   */
  public static class EnergySystemBuilder {

    private double batteryLevel;
    private static final double MAX_BATTERY_LEVEL = 100.0;
    private static final double MIN_BATTERY_LEVEL = 0.0;
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

    public EnergySystemBuilder setBatteryLevel(double batteryLevel) {
      if (batteryLevel > MAX_BATTERY_LEVEL || batteryLevel < MIN_BATTERY_LEVEL) {
        throw new IllegalArgumentException(
            String.format("Заряд батареи не может быть больше %.2f и меньше %.2f",
                MAX_BATTERY_LEVEL, MIN_BATTERY_LEVEL));
      }
      this.batteryLevel = batteryLevel / 100.0;
      return this;
    }

    public EnergySystemBuilder() {
      this.batteryLevel = generateBatteryLevel();
    }

    public EnergySystem build() {
      return new EnergySystem(this);
    }

  }
}