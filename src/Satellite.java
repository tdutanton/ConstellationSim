import java.util.Random;

public abstract class Satellite {
    protected String name;
    protected boolean isActive;
    protected double batteryLevel;

    private static final Random RANDOM = new Random();

    private static final double MIN_POSSIBLE_BATTERY_FOR_ACTIVATE = 0.15;

    protected static boolean consolePrintMode = false;

    public static void SetConsolePrintMode(boolean state) {
        consolePrintMode = state;
    }

    protected Satellite(String aName, int aNumber) {
        name = generateName(aName, aNumber);
        isActive = false;
        batteryLevel = generateBatteryLevel();
        if (consolePrintMode)
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
            if (consolePrintMode) System.out.printf("✅ %s: Активация успешна%n", name);
            return true;
        } else if (isActive) {
            if (consolePrintMode) System.out.printf("⚠️ %s: Активация уже была произведена ранее%n", name);
            return true;
        } else {
            if (consolePrintMode)
                System.out.printf("⛔ %s: Ошибка активации (заряд: %.0f%%)%n", name, batteryLevel * 100);
            return false;
        }
    }

    protected void handleChangeBatteryLevel() {
        if (batteryLevel <= MIN_POSSIBLE_BATTERY_FOR_ACTIVATE && isActive) deactivate();
    }

    protected abstract void performMission();
}