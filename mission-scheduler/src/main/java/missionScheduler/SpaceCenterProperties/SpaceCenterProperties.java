package missionScheduler.SpaceCenterProperties;

import java.util.List;
import missionScheduler.Domain.TargetType.TargetType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.space-center-service")
public record SpaceCenterProperties(
    String url,
    List<MissionConfig> missions
) {

  public record MissionConfig(
      TargetType targetType,
      String constellationName,
      String satelliteName,
      String cron
  ) {

  }
}