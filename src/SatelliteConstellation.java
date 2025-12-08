import java.util.ArrayList;
import java.util.List;

public class SatelliteConstellation {
    private final String constellationName;
    private final List<Satellite> satellites;

    private static final String NEW_LINE_DELIM_CONSTELLATION = "---------------------";
    private static final String DEFAULT_CONSTELLATION_NAME = "RU Basic";

    public SatelliteConstellation(String name) {
        constellationName = name;
        this.satellites = new ArrayList<>();
        printConstellationCtor();
    }

    public SatelliteConstellation() {
        constellationName = DEFAULT_CONSTELLATION_NAME;
        this.satellites = new ArrayList<>();
        printConstellationCtor();
    }

    private void printConstellationCtor() {
        System.out.printf("Создана спутниковая группировка: %s%n", constellationName);
    }

    public void addSatellite(Satellite satellite) {
        satellites.add(satellite);
        System.out.printf("%s добавлен в группировку '%s'%n", satellite.getName(), constellationName);
    }

    public void executeAllMissions() {
        System.out.printf("Выполнение миссий группировки %s%n", constellationName);
        System.out.println(NEW_LINE_DELIM_CONSTELLATION);
        for (Satellite s : satellites) {
            s.performMission();
        }
    }

    public List<Satellite> getSatellites() {
        return satellites;
    }

    public void activateSatellites() {
        System.out.println("АКТИВАЦИЯ СПУТНИКОВ");
        System.out.println(NEW_LINE_DELIM_CONSTELLATION);
        satellites.forEach(Satellite::activate);
        System.out.println(NEW_LINE_DELIM_CONSTELLATION);
    }

}
