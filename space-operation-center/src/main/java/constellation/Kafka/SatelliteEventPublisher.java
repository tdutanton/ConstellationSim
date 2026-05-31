package constellation.Kafka;

import constellation.Model.Domain.Satellite.Satellite;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SatelliteEventPublisher {

  private final OutboxEventRepository outboxRepository;

  public void publishSatelliteAdded(Satellite satellite) {
    String payload = String.format(
        "{\"eventId\":\"%s\",\"type\":\"SATELLITE_ADDED\",\"satelliteId\":%d}",
        java.util.UUID.randomUUID(), satellite.getId());
    OutboxEvent event = new OutboxEvent(
        String.valueOf(satellite.getId()), "CREATED", payload);
    outboxRepository.save(event);
    log.info("Saved outbox event: satellite {} added", satellite.getId());
  }

  public void publishSatelliteRemoved(Long satelliteId) {
    String payload = String.format(
        "{\"eventId\":\"%s\",\"type\":\"SATELLITE_REMOVED\",\"satelliteId\":%d}",
        java.util.UUID.randomUUID(), satelliteId);
    OutboxEvent event = new OutboxEvent(
        String.valueOf(satelliteId), "DELETED", payload);
    outboxRepository.save(event);
    log.info("Saved outbox event: satellite {} removed", satelliteId);
  }
}
