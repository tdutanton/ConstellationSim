package constellation.Domain.Internal.EnergySystem;

import java.util.Random;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class EnergySystemBuilder {

  private static final double MAX_BATTERY_LEVEL = 100.0;
  private static final double MIN_BATTERY_LEVEL = 0.0;
  private EnergySystem energySystem = new EnergySystem();

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

  public void setBatteryLevel(double batteryLevel) {
    if (batteryLevel > MAX_BATTERY_LEVEL || batteryLevel < MIN_BATTERY_LEVEL) {
      throw new IllegalArgumentException(
          String.format("Заряд батареи не может быть больше %.2f и меньше %.2f",
              MAX_BATTERY_LEVEL, MIN_BATTERY_LEVEL));
    }
    energySystem.batteryLevel = batteryLevel;
  }

  public void setBatteryRandomLevel() {
    energySystem.batteryLevel = generateBatteryLevel();
  }

  public EnergySystem build() {
    return energySystem;
  }

}
