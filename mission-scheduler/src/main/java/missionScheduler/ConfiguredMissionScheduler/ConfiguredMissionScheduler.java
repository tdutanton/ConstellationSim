package missionScheduler.ConfiguredMissionScheduler;

import jakarta.annotation.PostConstruct;
import missionScheduler.Clients.SpaceOperationClient;
import missionScheduler.Domain.MissionRequest.MissionRequest;
import missionScheduler.Domain.MissionRequest.MissionRequestSatName;
import missionScheduler.Domain.TargetType.TargetType;
import missionScheduler.SpaceCenterProperties.SpaceCenterProperties;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
public class ConfiguredMissionScheduler {

  private final SpaceCenterProperties properties;
  private final SpaceOperationClient client;
  private final TaskScheduler taskScheduler;

  public ConfiguredMissionScheduler(
      SpaceCenterProperties properties,
      SpaceOperationClient client,
      TaskScheduler taskScheduler) {
    this.properties = properties;
    this.client = client;
    this.taskScheduler = taskScheduler;
  }

  @PostConstruct
  public void scheduleMissions() {
    for (var mission : properties.missions()) {
      try {
        scheduleMission(mission);
      } catch (IllegalArgumentException e) {
        System.out.println("Пропущена некорректная миссия: " + e);
      }
    }
  }

  private void scheduleMission(SpaceCenterProperties.MissionConfig mission) {
    var trigger = new CronTrigger(mission.cron());
    taskScheduler.schedule(() -> {
      try {
        System.out.println("Запуск миссии");
        var request = buildMissionRequest(mission);
        client.executeMission(request);
        System.out.println("Миссия выполнена успешно");
      } catch (IllegalArgumentException e) {
        System.out.println("Ошибка при создании миссии " + e);
      } catch (Exception e) {
        System.out.println("Ошибка при выполнении миссии");
      }
    }, trigger);

    System.out.println("Запланирована миссия с cron");
  }

  private MissionRequest buildMissionRequest(SpaceCenterProperties.MissionConfig mission) {
    if (mission.targetType() == TargetType.CONSTELLATION && mission.satelliteName() != null) {
      throw new IllegalArgumentException(
          "Для типа миссии Группировка нельзя указывать имя спутника");
    }
    if (mission.targetType() == TargetType.SINGLE_SATELLITE && mission.satelliteName() == null) {
      throw new IllegalArgumentException(
          "Для типа миссии Спутник должны быть указаны и имя группировки и спутника");
    }
    if (mission.constellationName() == null) {
      throw new IllegalArgumentException(
          "Имя группировки должно быть заполнено");
    }

    return new MissionRequestSatName(
        mission.targetType(),
        mission.constellationName(),
        mission.satelliteName()
    );
  }

}
