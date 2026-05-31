package constellation.Kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import constellation.events.proto.SatelliteEvent;
import constellation.events.proto.SatelliteEventType;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {

  private final OutboxEventRepository outboxRepository;
  private final KafkaTemplate<String, byte[]> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Value("${KAFKA_TOPIC_SATELLITE_EVENTS:satellite-events}")
  private String topic;

  @Scheduled(fixedDelayString = "${outbox.polling.interval:5000}")
  public void processOutbox() {
    List<OutboxEvent> pendingEvents = outboxRepository.findByStatus("PENDING");
    for (OutboxEvent outboxEvent : pendingEvents) {
      try {
        SatelliteEvent event = buildEvent(outboxEvent);
        byte[] payload = event.toByteArray();
        kafkaTemplate.send(topic, outboxEvent.getAggregateId(), payload)
            .get(10, java.util.concurrent.TimeUnit.SECONDS);
        markAsSent(outboxEvent);
        log.info("Outbox event {} sent to Kafka, satellite {}",
            outboxEvent.getId(), outboxEvent.getAggregateId());
      } catch (Exception e) {
        log.error("Failed to process outbox event {}", outboxEvent.getId(), e);
      }
    }
  }

  @Transactional
  protected void markAsSent(OutboxEvent event) {
    event.setStatus("SENT");
    outboxRepository.save(event);
  }

  private SatelliteEvent buildEvent(OutboxEvent outboxEvent)
      throws JsonProcessingException {
    JsonNode json = objectMapper.readTree(outboxEvent.getPayload());
    SatelliteEventType type = SatelliteEventType.valueOf(
        json.get("type").asText());
    return SatelliteEvent.newBuilder()
        .setEventId(json.get("eventId").asText())
        .setType(type)
        .setSatelliteId(json.get("satelliteId").asLong())
        .build();
  }
}
