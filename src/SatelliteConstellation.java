import java.util.ArrayList;
import java.util.List;

public class SatelliteConstellation {
    private final String constellationName;
    private final List<Satellite> satellites;

    private static final String NEW_LINE_DELIM_CONSTELLATION = "---------------------";
    private static final String DEFAULT_CONSTELLATION_NAME = "RU Basic";

    private static boolean consolePrintMode = false;

    public static void SetConsolePrintMode(boolean state) {
        consolePrintMode = state;
    }

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
        if (consolePrintMode) System.out.printf("Создана спутниковая группировка: %s%n", constellationName);
    }

    public void addSatellite(Satellite satellite) {
        satellites.add(satellite);
        if (consolePrintMode)
            System.out.printf("%s добавлен в группировку '%s'%n", satellite.getName(), constellationName);
    }

    public void executeAllMissions() {
        if (consolePrintMode) {
            System.out.printf("Выполнение миссий группировки %s%n", constellationName);
            System.out.println(NEW_LINE_DELIM_CONSTELLATION);
        }
        for (Satellite s : satellites) {
            s.performMission();
        }
    }

    public List<Satellite> getSatellites() {
        return satellites;
    }

    public void activateSatellites() {
        if (consolePrintMode) {
            System.out.println("АКТИВАЦИЯ СПУТНИКОВ");
            System.out.println(NEW_LINE_DELIM_CONSTELLATION);
        }
        if (!satellites.isEmpty()) satellites.forEach(Satellite::activate);
        else {
            if (consolePrintMode)
                System.out.printf("⛔ Активация спутников невозможна. В группировке %s отсутствуют спутники", constellationName);
        }
        if (consolePrintMode) System.out.println(NEW_LINE_DELIM_CONSTELLATION);
    }

}
