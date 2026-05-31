package TelemetryService.Kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import constellation.events.proto.SatelliteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SatelliteEventConsumer {

  private final SatelliteRegistry satelliteRegistry;
  private final InboxEventRepository inboxRepository;

  @KafkaListener(
      topics = "${KAFKA_TOPIC_SATELLITE_EVENTS:satellite-events}",
      groupId = "${KAFKA_GROUP_ID:telemetry-service-group}",
      concurrency = "1"
  )
  @Transactional
  public void handleSatelliteEvent(byte[] payload) {
    try {
      SatelliteEvent event = SatelliteEvent.parseFrom(payload);
      String eventId = event.getEventId();

      if (inboxRepository.existsById(eventId)) {
        log.info("Событие {} уже обработано, пропускаем", eventId);
        return;
      }

      log.info("Получено событие: тип={} id спутника={}", event.getType(), event.getSatelliteId());

      InboxEvent inboxEvent = new InboxEvent(
          eventId, event.getSatelliteId(), event.getType().name());
      inboxRepository.save(inboxEvent);

      switch (event.getType()) {
        case SATELLITE_ADDED -> {
          satelliteRegistry.addSatellite(event.getSatelliteId());
          log.info("Добавлен спутник (id={}) в поток телеметрии", event.getSatelliteId());
        }
        case SATELLITE_REMOVED -> {
          satelliteRegistry.removeSatellite(event.getSatelliteId());
          log.info("Удален спутник (id={}) из потока телеметрии", event.getSatelliteId());
        }
        case UNRECOGNIZED -> log.warn("Получен неизвестный тип события");
      }
    } catch (InvalidProtocolBufferException e) {
      log.error("Ошибка парсинга protobuf", e);
    } catch (Exception e) {
      log.error("Ошибка обработки события", e);
    }
  }
}
