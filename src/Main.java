public class Main {
    public static void main(String[] args) {
        final String DIVIDER = "---------------------";
        final String SatelliteIcon = "\uD83D\uDEF0\uFE0F";

        Satellite.SetConsolePrintMode(true);
        SatelliteConstellation.SetConsolePrintMode(true);

        System.out.printf("%1$s %1$s ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ %1$s %1$s%n",
                SatelliteIcon);
        System.out.println("=====================");

        System.out.println("Создание специализированных спутников:");
        System.out.println(DIVIDER);
        CommunicationSatellite comSatFirst = new CommunicationSatellite(500);
        CommunicationSatellite comSatSecond = new CommunicationSatellite(1000);
        ImagingSatellite imagSatFirst = new ImagingSatellite(2.5);
        ImagingSatellite imagSatSecond = new ImagingSatellite(1.0);
        ImagingSatellite imagSatThird = new ImagingSatellite(0.5);

        System.out.println(DIVIDER);
        SatelliteConstellation team = new SatelliteConstellation("RU Basic");
        System.out.println(DIVIDER);

        System.out.println("Формирование группировки:");
        System.out.println(DIVIDER);
        team.addSatellite(comSatFirst);
        team.addSatellite(comSatSecond);
        team.addSatellite(imagSatFirst);
        team.addSatellite(imagSatSecond);
        team.addSatellite(imagSatThird);

        System.out.println(DIVIDER);
        System.out.println(team.getSatellites());

        System.out.println(DIVIDER);
        team.activateSatellites();

        team.executeAllMissions();

        System.out.println(DIVIDER);
        System.out.println(team.getSatellites());
    }
}
