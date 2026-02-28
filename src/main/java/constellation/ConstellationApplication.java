package constellation;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.CommunicationSatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.ImagingSatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import constellation.Service.SpaceOperationCenterService.AddSatelliteRequest;
import constellation.Service.SpaceOperationCenterService.ConstellationRequest;
import constellation.Service.SpaceOperationCenterService.MissionRequest;
import constellation.Service.SpaceOperationCenterService.SpaceOperationCenterService;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ConstellationApplication {

  public static void main(String[] args) {
    Satellite.setConsolePrintMode(true);
    SatelliteConstellation.setConsolePrintMode(true);
    final String SatelliteIcon = "\uD83D\uDEF0\uFE0F";
    final String DIVIDER = "---------------------";
    final String TEAM_FIRST = "Орбита-1";
    final String TEAM_SECOND = "Орбита-2";
    System.out.printf("%1$s %1$s ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ %1$s %1$s%n",
        SatelliteIcon);
    System.out.println("=====================");
    ConfigurableApplicationContext context = SpringApplication.run(ConstellationApplication.class,
        args);
    SpaceOperationCenterService centralService = context.getBean(
        SpaceOperationCenterService.class);

    System.out.println("Создание группировок и добавление в них специализированных спутников:");
    System.out.println(DIVIDER);
    centralService.addSatellite(
        new AddSatelliteRequest(TEAM_FIRST,
            List.of(new CommunicationSatelliteParam(SatelliteType.COMMUNICATION, "Связной", 96,
                400))));

    centralService.addSatellite(new AddSatelliteRequest(TEAM_SECOND, List.of(
        new CommunicationSatelliteParam(
            SatelliteType.COMMUNICATION, "Связной", 70, 500))));

    centralService.addSatellite(new AddSatelliteRequest(TEAM_FIRST, List.of(
        new ImagingSatelliteParam(
            SatelliteType.IMAGE, "IMG", 96, 1.0))));

    centralService.addSatellite(new AddSatelliteRequest(TEAM_SECOND, List.of(
        new ImagingSatelliteParam(
            SatelliteType.IMAGE, "IMG", 60, 0.5))));

    centralService.addSatellite(new AddSatelliteRequest(TEAM_SECOND, List.of(
        new ImagingSatelliteParam(
            SatelliteType.IMAGE, "IMG", 87, 1.5))));
    System.out.println();
    System.out.println(DIVIDER);

    System.out.println();
    System.out.println(DIVIDER);
    centralService.activateSatellites(new ConstellationRequest(TEAM_FIRST));

    System.out.println();
    System.out.println(DIVIDER);
    centralService.executeMission(
        new MissionRequest(List.of(TEAM_FIRST), SatelliteType.COMMUNICATION));

    System.out.println();
    System.out.println(DIVIDER);
    centralService.showConstellationStatus(new ConstellationRequest(TEAM_FIRST));

    System.out.println();
    System.out.println(DIVIDER);
    centralService.activateSatellites(new ConstellationRequest(TEAM_SECOND));

    System.out.println();
    System.out.println(DIVIDER);
    centralService.executeMission(new MissionRequest(List.of(TEAM_SECOND), SatelliteType.IMAGE));

    System.out.println();
    System.out.println(DIVIDER);
    centralService.showConstellationStatus(new ConstellationRequest(TEAM_SECOND));
  }
}
