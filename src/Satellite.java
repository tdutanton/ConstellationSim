import java.util.Random;

public abstract class Satellite {
    protected String name;
    protected boolean isActive;
    protected double batteryLevel;

    private static final Random RANDOM = new Random();

    private static final double MIN_POSSIBLE_BATTERY_FOR_ACTIVATE = 0.15;
    private static final double ZERO_BATTERY = 0.0;
    private static final double MAX_BATTERY = 1.0;
    protected static final String NEW_LINE_DELIM_SATELLITE = "---------------------";

    protected Satellite(String aName, int aNumber) {
        name = generateName(aName, aNumber);
        isActive = false;
        batteryLevel = generateBatteryLevel();
        System.out.printf("\uD83D\uDEF0\uFE0F Создан спутник: %s (заряд: %.0f%%)%n", name, batteryLevel * 100.0);
    }

    public void deactivate() {
        isActive = false;
    }

    public String getName() {
        return name;
    }

    protected double generateBatteryLevel() {
        return RANDOM.nextDouble();
    }

    private String generateName(String name, int number) {
        return name + "-" + number;
    }

    public void consumeBattery(double amount) {
        if (amount < 0.0)
            throw new IllegalArgumentException("Значение для снижения заряда батареи не должно быть отрицательным");
        batteryLevel = Math.max(0.0, this.batteryLevel - amount);
    }

    public boolean activate() {
        if (batteryLevel > MIN_POSSIBLE_BATTERY_FOR_ACTIVATE && !isActive) {
            isActive = true;
            System.out.printf("✅ %s: Активация успешна%n", name);
            return true;
        } else if (isActive) {
            System.out.printf("⚠️ %s: Активация уже была произведена ранее%n", name);
            return true;
        } else {
            System.out.printf("⛔ %s: Ошибка активации (заряд: %.0f%%)%n", name, batteryLevel * 100);
            return false;
        }
    }

    protected void handleChangeBatteryLevel() {
        if (batteryLevel <= MIN_POSSIBLE_BATTERY_FOR_ACTIVATE && isActive) isActive = false;
    }

    protected abstract void performMission();
}