public class CommunicationSatellite extends Satellite {
    private final double bandwidth;

    private static final String DEFAULT_NAME = "Связь";
    private static int SERIAL_NUMBER = 1;
    private static final double BATTERY_PER_MISSION = 0.09;

    public CommunicationSatellite(double aBandwidth) {
        if (aBandwidth < 0.0)
            throw new IllegalArgumentException("Пропускная способность не должна быть отрицательной");

        super(DEFAULT_NAME, SERIAL_NUMBER);
        bandwidth = aBandwidth;
        SERIAL_NUMBER++;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{bandwidth=%s, name='%s', isActive=%s, batteryLevel=%.2f}",
                this.getClass().getSimpleName(),
                bandwidth,
                name,
                isActive,
                batteryLevel
        );
    }

    public double getBandwidth() {
        return bandwidth;
    }

    @Override
    public void performMission() {
        if (isActive) {
            if (consolePrintMode)
                System.out.printf("✅ %s: Передача данных со скоростью %.1f Мбит/с%n", name, bandwidth);
            sendData(bandwidth);
            consumeBattery(BATTERY_PER_MISSION);
            handleChangeBatteryLevel();
        } else {
            if (consolePrintMode) System.out.printf("⛔ %s не может передать данные - не активен%n", name);
        }
    }

    private void sendData(double data) {
        if (consolePrintMode) System.out.printf("✅ %s отправил %.0f Мбит данных!%n", name, data);
    }
}
