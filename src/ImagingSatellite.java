public class ImagingSatellite extends Satellite{
    private double resolution;
    private int photosTaken;

    private static final String DEFAULT_NAME = "ДЗЗ";
    private static final int SERIAL_NUMBER = 1;

    public double getResolution() {
        return resolution;
    }

    public int getPhotosTaken() {
        return photosTaken;
    }

    @Override
    public void performMission() {
        System.out.println("TODO imaging performMission");
    }

    private void takePhoto() {
        System.out.println("TODO takePhoto");
    }
}
