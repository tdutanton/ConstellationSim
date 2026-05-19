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

  // KafkaTemplate из KafkaProducerConfig, внедряется через конструктор через @RequiredArgsConstructor
  private final KafkaTemplate<String, byte[]> kafkaTemplate;

  // имя топика - из конфига или satellite-events по умолчанию
  @Value("${KAFKA_TOPIC_SATELLITE_EVENTS:satellite-events}")
  private String topic;

  // публикация события о добавлении спутника
  public void publishSatelliteAdded(Satellite satellite) {
    SatelliteEvent event = SatelliteEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setType(SatelliteEventType.SATELLITE_ADDED)
        .setSatelliteId(satellite.getId())
        .build();

    // конвертация protobuf в байты
    byte[] payload = event.toByteArray();

    // отправка в kafka - топик, ключ, значение
    kafkaTemplate.send(topic, String.valueOf(satellite.getId()), payload)
        .whenComplete((result, ex) -> { // асинхронный колбэк
          if (ex != null) {
            log.error("Failed to send satellite ADDED event", ex);
          }
          // если все ок, то result содержит метаданные отправки
        });
  }

  // публикация события об удалении спутника
  public void publishSatelliteRemoved(Long satelliteId) {
    SatelliteEvent event = SatelliteEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setType(SatelliteEventType.SATELLITE_REMOVED)
        .setSatelliteId(satelliteId)
        .build();

    kafkaTemplate.send(topic, String.valueOf(satelliteId), event.toByteArray());
  }
}
