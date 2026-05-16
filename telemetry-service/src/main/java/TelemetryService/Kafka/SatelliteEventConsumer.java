package TelemetryService.Kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import constellation.events.proto.SatelliteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SatelliteEventConsumer {

  private final SatelliteRegistry satelliteRegistry;

  @KafkaListener(
      topics = "${KAFKA_TOPIC_SATELLITE_EVENTS:satellite-events}",
      groupId = "${KAFKA_GROUP_ID:telemetry-service-group}",
      concurrency = "1"
  )
  public void handleSatelliteEvent(byte[] payload) { // ← принимаем byte[]
    try {
      // ✅ Парсим byte[] → Protobuf-объект
      SatelliteEvent event = SatelliteEvent.parseFrom(payload);

      log.info("Received event: type={} satelliteId={}",
          event.getType(), event.getSatelliteId());

      switch (event.getType()) {
        case SATELLITE_ADDED -> {
          satelliteRegistry.addSatellite(event.getSatelliteId());
          log.info("✅ Added (id={}) to telemetry stream",
              event.getSatelliteId());
        }
        case SATELLITE_REMOVED -> {
          satelliteRegistry.removeSatellite(event.getSatelliteId());
          log.info("❌ Removed  (id={}) from telemetry stream",
              event.getSatelliteId());
        }
        case UNRECOGNIZED -> log.warn("⚠️ Received unrecognized event type");
      }
    } catch (InvalidProtocolBufferException e) {
      log.error("Failed to parse Protobuf message", e);
    } catch (Exception e) {
      log.error("Failed to process satellite event", e);
    }
  }
}
