package constellation.Kafka;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для таблицы outbox_event: - id; - event_type (add, delete); - payload; - текст
 * (json); - created_at; - status (pending, sent)
 */
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

  List<OutboxEvent> findByStatus(String status);
}
