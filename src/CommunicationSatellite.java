public class CommunicationSatellite extends Satellite {
    private double bandwidth;

    private static final String DEFAULT_NAME = "Связь";
    private static final int SERIAL_NUMBER = 1;

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
