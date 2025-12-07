public class ImagingSatellite extends Satellite{
    private double resolution;
    private int photosTaken;

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
