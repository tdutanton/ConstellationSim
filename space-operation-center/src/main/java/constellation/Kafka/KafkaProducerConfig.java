package constellation.Kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

  // адрес для kafka
  @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
  private String bootstrapServers;

  // ProducerFactory - интерфейс-фабрика для создания producer'ов
  @Bean
  public ProducerFactory<String, byte[]> producerFactory() {
    Map<String, Object> config = new HashMap<>();

    // где находится kafka сервер
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    // как сериализовать ключ сообщения (превратить в байты)
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // как сериализовать тело сообщения
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

    // что делать если не удалась отправка
    // пробует еще 3 раза
    config.put(ProducerConfig.RETRIES_CONFIG, 3);
    // ждет 1 секунду между попытками
    config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);

    // реализация фабрики от spring
    return new DefaultKafkaProducerFactory<>(config);
  }

  @Bean
  public KafkaTemplate<String, byte[]> kafkaTemplate() {
    // KafkaTemplate - spring обертка над низким уровнем kafka api
    return new KafkaTemplate<>(producerFactory());
  }
}
