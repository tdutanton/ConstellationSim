package missionScheduler.Clients;

import missionScheduler.Domain.MissionRequest.MissionRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SpaceOperationClient {

  private final RestClient restClient;

  public SpaceOperationClient(RestClient restClient) {
    this.restClient = restClient;
  }

  public void executeMission(MissionRequest request) {
    restClient.post()
        .uri("/missions")
        .contentType(MediaType.APPLICATION_JSON)
        .body(request)
        .retrieve()
        .toBodilessEntity();
  }
}