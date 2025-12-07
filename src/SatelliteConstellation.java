import java.util.List;

public class SatelliteConstellation {
    private String constellationName;
    private List<Satellite> satellites;

    public void addSatellite(Satellite satellite) {
        System.out.println("TODO addSatellite" + satellite.getName());
    }

    public void executeAllMissions() {
        System.out.println("TODO executeAllMissions");
    }

    public List<Satellite> getSatellites() {
        return satellites;
    }

}
