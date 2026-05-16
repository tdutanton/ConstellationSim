package TelemetryService.Kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {


  // ✅ @Value разрешит ${...} в реальное значение при старте
  @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
  private String bootstrapServers;

  @Value("${KAFKA_GROUP_ID:telemetry-service-group}")
  private String groupId;

  @Bean
  public ConsumerFactory<String, byte[]> consumerFactory() {
    Map<String, Object> props = new HashMap<>();

    // ✅ Явно передаём уже разрешённое значение
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        ByteArrayDeserializer.class.getName());

    // Retry-настройки
    props.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, "1000");
    props.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, "10000");
    props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "30000");

    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, byte[]>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}
