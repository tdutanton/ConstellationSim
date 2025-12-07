public class ImagingSatellite extends Satellite{
    private final double resolution;
    private int photosTaken;

    private static final String DEFAULT_NAME = "ДЗЗ";
    private static int SERIAL_NUMBER = 1;

    public ImagingSatellite(double aResolution) {
        if (aResolution < 0.0)
            throw new IllegalArgumentException("Разрешение не должно быть отрицательным");

        super(DEFAULT_NAME, SERIAL_NUMBER);
        resolution = aResolution;
        photosTaken = 0;
        SERIAL_NUMBER++;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{resolution=%.1f, photosTaken=%d, name='%s' isActive=%s, batteryLevel=%.2f}",
                this.getClass().getSimpleName(),
                resolution,
                photosTaken,
                name,
                isActive,
                batteryLevel
        );
    }

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
        photosTaken++;
    }
}
