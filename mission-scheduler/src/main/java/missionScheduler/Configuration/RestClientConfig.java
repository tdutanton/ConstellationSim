package missionScheduler.Configuration;

import missionScheduler.SpaceCenterProperties.SpaceCenterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  private final SpaceCenterProperties spaceCenterProperties;

  public RestClientConfig(SpaceCenterProperties properties) {
    this.spaceCenterProperties = properties;
  }

  @Bean
  public RestClient spaceOperationRestClient() {
    return RestClient.builder()
        .baseUrl(spaceCenterProperties.url())
        .build();
  }
}
