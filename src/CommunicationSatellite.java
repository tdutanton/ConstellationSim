public class CommunicationSatellite extends Satellite {
    private final double bandwidth;

    private static final String DEFAULT_NAME = "Связь";
    private static int SERIAL_NUMBER = 1;

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
        System.out.println("TODO communication performMission");
    }

    private void sendData(double data) {
        System.out.printf("TODO sendData %f", data);
    }
}
