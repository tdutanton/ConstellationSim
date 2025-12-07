import java.util.List;

public class Main {
    public static void main(String[] args) {
        ImagingSatellite s = new ImagingSatellite(100.0);
        ImagingSatellite s2 = new ImagingSatellite(100.0);

        CommunicationSatellite c = new CommunicationSatellite(500);

        System.out.println(c);

        SatelliteConstellation team = new SatelliteConstellation("RUR");
        team.addSatellite(s2);
        team.addSatellite(c);

        List<Satellite> satellites = team.getSatellites();
        System.out.println(satellites);
        team.activateSatellites();
    }

}
