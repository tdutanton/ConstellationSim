package constellation;

import constellation.Domain.Constellation.SatelliteConstellation;
import constellation.Domain.Satellite.Satellite;
import constellation.Domain.SatelliteFactory.CommunicationSatelliteFactory;
import constellation.Domain.SatelliteFactory.ImagingSatelliteFactory;
import constellation.Domain.SatelliteFactory.SatelliteFactory;
import constellation.Repository.ConstellationRepository;
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
    ConfigurableApplicationContext context = SpringApplication.run(ConstellationApplication.class,
        args);
    ConstellationRepository repo = context.getBean(ConstellationRepository.class);
    SpaceOperationCenterService service = context.getBean(SpaceOperationCenterService.class);
    SatelliteFactory commFactory = new CommunicationSatelliteFactory();
    SatelliteFactory imgFactory = new ImagingSatelliteFactory();

    System.out.println("Создание специализированных спутников:");
    System.out.println(DIVIDER);
    Satellite comSatFirst = commFactory.createSatellite("Связной", 90);
    Satellite imagSatFirst = imgFactory.createSatellite("IMG", 50);
    Satellite comSatSecond = commFactory.createSatelliteWithParameter("Связной", 100, 400);
    Satellite imagSatSecond = imgFactory.createSatelliteWithParameter("IMG", 50, 1.0);
    Satellite imagSatThird = imgFactory.createSatelliteWithParameter("IMG", 78, 0.5);
    System.out.println(DIVIDER);

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
  }
}
