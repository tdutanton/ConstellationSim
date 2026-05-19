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

// настройка стороны слушателя сообщений от kafka
@Configuration
@EnableKafka // подключение поддержки @Kafkalistener
public class KafkaConsumerConfig {


  // адрес для kafka
  @Value("${spring.kafka.bootstrap-servers:kafka:9092}")
  private String bootstrapServers;

  // имя группы потребителей
  @Value("${KAFKA_GROUP_ID:telemetry-service-group}")
  private String groupId;

  @Bean
  public ConsumerFactory<String, byte[]> consumerFactory() {
    Map<String, Object> props = new HashMap<>();

    // где находится kafka
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    // группа потребителей
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    // если сервис запустился впервые - читать с самого начала
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    // как делать десериализацию
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        ByteArrayDeserializer.class.getName());

    // настройки переподключения при сбоях
    // ждет 1 с перед повторным подключением
    props.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, "1000");
    // максимальное ожидание 10 с
    props.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, "10000");
    // таймаут запроса 30 с
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
