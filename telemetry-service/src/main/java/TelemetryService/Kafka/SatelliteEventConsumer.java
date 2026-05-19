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

  // внедряется автоматически
  private final SatelliteRegistry satelliteRegistry;

  // аннотация делает метод слушателем kafka
  @KafkaListener(
      topics = "${KAFKA_TOPIC_SATELLITE_EVENTS:satellite-events}", // какой топик слушать
      groupId = "${KAFKA_GROUP_ID:telemetry-service-group}", // к какой группе принадлежать
      concurrency = "1" // сколько потоков для обработки
  )
  public void handleSatelliteEvent(byte[] payload) { // kafka сама передаст сюда байты payload
    try {
      // десерализация - из байт в protobuf
      SatelliteEvent event = SatelliteEvent.parseFrom(payload);

      log.info("Получено событие: тип={} id спутника={}",
          event.getType(), event.getSatelliteId());
      // обработка в зависимости от типа события
      switch (event.getType()) {
        case SATELLITE_ADDED -> {
          satelliteRegistry.addSatellite(event.getSatelliteId());
          log.info("Добавлен спутник (id={}) в поток телеметрии",
              event.getSatelliteId());
        }
        case SATELLITE_REMOVED -> {
          satelliteRegistry.removeSatellite(event.getSatelliteId());
          log.info("Удален спутник (id={}) из потока телеметрии",
              event.getSatelliteId());
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
