package constellation.Kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import constellation.events.proto.SatelliteEvent;
import constellation.events.proto.SatelliteEventType;
import java.util.List;
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
  // KafkaTemplate из KafkaProducerConfig, внедряется через конструктор через @RequiredArgsConstructor
  private final KafkaTemplate<String, byte[]> kafkaTemplate;
  private final ObjectMapper objectMapper;

  // имя топика - из конфига или satellite-events по умолчанию
  @Value("${KAFKA_TOPIC_SATELLITE_EVENTS:satellite-events}")
  private String topic;

  // работает по расписанию
  // в репозитории смотрит записи со статусом PENDING (которые надо обработать)
  // в цикле проходится по ним - создает protobuf структуры и через kafkaTemplate отправляет
  @Scheduled(fixedDelayString = "${outbox.polling.interval:5000}")
  public void processOutbox() {
    List<OutboxEvent> pendingEvents = outboxRepository.findByStatus("PENDING");
    for (OutboxEvent outboxEvent : pendingEvents) {
      try {
        // структура protobuf (event_id, SatelliteEventType (enum), satellite_id
        SatelliteEvent event = buildEvent(outboxEvent);
        byte[] payload = event.toByteArray();
        kafkaTemplate.send(topic, outboxEvent.getAggregateId(), payload)
            .get(10, java.util.concurrent.TimeUnit.SECONDS);
        // после отправки в репозитории транзакция по изменению поля на SENT
        markAsSent(outboxEvent);
        log.info("Outbox событие {} отправлено при помощи Kafka, спутник {}",
            outboxEvent.getId(), outboxEvent.getAggregateId());
      } catch (Exception e) {
        log.error("Ошибка обработки outbox события {}", outboxEvent.getId(), e);
      }
    }
  }

  // отмечает в таблице outbox_events - в строке изменяет поле на SENT
  @Transactional
  protected void markAsSent(OutboxEvent event) {
    event.setStatus("SENT");
    outboxRepository.save(event);
  }

  // структура protobuf (event_id, SatelliteEventType (enum), satellite_id
  private SatelliteEvent buildEvent(OutboxEvent outboxEvent)
      throws JsonProcessingException {
    // берется поле payload из outboxEvent
    JsonNode json = objectMapper.readTree(outboxEvent.getPayload());
    // берется из payload значение поля type
    SatelliteEventType type = SatelliteEventType.valueOf(
        json.get("type").asText());
    // берутся из payload значения других полей
    return SatelliteEvent.newBuilder()
        .setEventId(json.get("eventId").asText())
        .setType(type)
        .setSatelliteId(json.get("satelliteId").asLong())
        .build();
  }
}
