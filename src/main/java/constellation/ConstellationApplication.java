package constellation;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Model.Domain.Internal.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.CommunicationSatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.ImagingSatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import constellation.Repository.ConstellationRepository;
import constellation.Service.Satellite.SatelliteService;
import constellation.Service.SpaceOperationCenterService;
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
    try {
      ConfigurableApplicationContext context = SpringApplication.run(ConstellationApplication.class,
          args);
      ConstellationRepository repo = context.getBean(ConstellationRepository.class);
      SpaceOperationCenterService service = context.getBean(SpaceOperationCenterService.class);
      SatelliteService satelliteService = context.getBean(SatelliteService.class);

      System.out.println("Создание специализированных спутников:");
      System.out.println(DIVIDER);
      Satellite comSatFirst = satelliteService.createSatellite(new CommunicationSatelliteParam(
          SatelliteType.COMMUNICATION, "Связной", 96, 400));
      Satellite imagSatFirst = satelliteService.createSatellite(new ImagingSatelliteParam(
          SatelliteType.IMAGE, "IMG", 96, 1.0));
      Satellite comSatSecond = satelliteService.createSatellite(new CommunicationSatelliteParam(
          SatelliteType.COMMUNICATION, "Связной", 70, 500));
      Satellite imagSatSecond = satelliteService.createSatellite(new ImagingSatelliteParam(
          SatelliteType.IMAGE, "IMG", 60, 0.5));
      Satellite imagSatThird = satelliteService.createSatellite(new ImagingSatelliteParam(
          SatelliteType.IMAGE, "IMG", 87, 1.5));

      service.createAndSaveConstellation(TEAM_FIRST);
      service.createAndSaveConstellation(TEAM_SECOND);

      System.out.println();
      System.out.println(DIVIDER);
      System.out.println("\uD83D\uDCE1 ДОБАВЛЕНИЕ СПУТНИКОВ:");
      service.addSatelliteToConstellation(TEAM_FIRST, comSatFirst);
      service.addSatelliteToConstellation(TEAM_SECOND, comSatSecond);
      service.addSatelliteToConstellation(TEAM_FIRST, imagSatFirst);
      service.addSatelliteToConstellation(TEAM_SECOND, imagSatSecond);
      service.addSatelliteToConstellation(TEAM_SECOND, imagSatThird);

      System.out.println();
      System.out.println(DIVIDER);
      service.activateAllSatellites(TEAM_FIRST);

      System.out.println();
      System.out.println(DIVIDER);
      service.executeConstellationMission(TEAM_FIRST);

      System.out.println();
      System.out.println(DIVIDER);
      service.showConstellationStatus(TEAM_FIRST);

      System.out.println();
      System.out.println(DIVIDER);
      service.activateAllSatellites(TEAM_SECOND);

      System.out.println();
      System.out.println(DIVIDER);
      service.executeConstellationMission(TEAM_SECOND);

      System.out.println();
      System.out.println(DIVIDER);
      service.showConstellationStatus(TEAM_SECOND);
    } catch (SpaceOperationException e) {
      System.err.println("Произошла ошибка при запуске: " + e.getMessage());
    }
  }
}
