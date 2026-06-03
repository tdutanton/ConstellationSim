package constellation.Kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import constellation.Model.Domain.Satellite.Satellite;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SatelliteEventPublisher {

  private final OutboxEventRepository outboxRepository;
  private final ObjectMapper objectMapper;

  // отправляет в psql таблицу запись outboxEvent о добавлении спутника
  public void publishSatelliteAdded(Satellite satellite) {
    SatelliteEventPayload satelliteEventPayload = new SatelliteEventPayload(
        UUID.randomUUID().toString(),
        "SATELLITE_ADDED",
        satellite.getId()
    );
    try {
      String payloadJson = objectMapper.writeValueAsString(satelliteEventPayload);
      OutboxEvent event = new OutboxEvent(
          String.valueOf(satellite.getId()), "CREATED", payloadJson);
      outboxRepository.save(event);
      log.info("Сохранено outbox событие в репозиторий: спутник {} добавлен", satellite.getId());
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize outbox payload", e);
    }
  }

  // отправляет в psql таблицу запись outboxEvent об удалении спутника
  public void publishSatelliteRemoved(Long satelliteId) {
    SatelliteEventPayload satelliteEventPayload = new SatelliteEventPayload(
        UUID.randomUUID().toString(),
        "SATELLITE_REMOVED",
        satelliteId
    );
    try {
      String payloadJson = objectMapper.writeValueAsString(satelliteEventPayload);
      OutboxEvent event = new OutboxEvent(
          String.valueOf(satelliteId), "DELETED", payloadJson);
      outboxRepository.save(event);
      log.info("Сохранено outbox событие в репозиторий: спутник {} удален", satelliteId);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize outbox payload", e);
    }
  }
}
