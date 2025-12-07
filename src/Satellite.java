public abstract class Satellite {
    protected String name;
    protected boolean isActive;
    protected double batteryLevel;

    protected static double MIN_POSSIBLE_BATTERY_FOR_ACTIVATE = 15.0;

    public void deactivate() {
        isActive = false;
    }

    public String getName() {
        return name;
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
            System.out.printf("%s: Активация успешна%n", name);
            return true;
        } else if (isActive) {
            System.out.printf("%s: Активация уже была произведена ранее%n", name);
            return true;
        } else {
            System.out.printf("%s: Ошибка активации (заряд: %.1f%%)%n", name, batteryLevel);
            return false;
        }
    }

    protected abstract void performMission();
}