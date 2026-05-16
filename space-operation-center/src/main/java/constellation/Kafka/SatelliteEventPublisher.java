package constellation.Kafka;

import constellation.Model.Domain.Satellite.Satellite;
import constellation.events.proto.SatelliteEvent;
import constellation.events.proto.SatelliteEventType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SatelliteEventPublisher {

  private final KafkaTemplate<String, byte[]> kafkaTemplate; // ← byte[]
  @Value("${KAFKA_TOPIC_SATELLITE_EVENTS:satellite-events}")
  private String topic;

  public void publishSatelliteAdded(Satellite satellite) {
    SatelliteEvent event = SatelliteEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setType(SatelliteEventType.SATELLITE_ADDED)
        .setSatelliteId(satellite.getId())
        .build();

    // Конвертируем Protobuf → byte[]
    byte[] payload = event.toByteArray();

    kafkaTemplate.send(topic, satellite.getName(), payload)
        .whenComplete((result, ex) -> {
          if (ex != null) {
            log.error("Failed to send satellite ADDED event", ex);
          }
        });
  }

  public void publishSatelliteRemoved(Long satelliteId, String satelliteName) {
    SatelliteEvent event = SatelliteEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setType(SatelliteEventType.SATELLITE_REMOVED)
        .setSatelliteId(satelliteId)
        .build();

    kafkaTemplate.send(topic, satelliteName, event.toByteArray());
  }
}
