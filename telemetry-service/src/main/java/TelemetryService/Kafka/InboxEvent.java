package TelemetryService.Kafka;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inbox_events")
@Getter
@Setter
@NoArgsConstructor
public class InboxEvent {

  @Id
  @Column(name = "event_id", nullable = false)
  private String eventId;

  @Column(name = "aggregate_id", nullable = false)
  private Long aggregateId;

  @Column(name = "event_type", nullable = false)
  private String eventType;

  @Column(name = "processed_at", nullable = false)
  private Instant processedAt;

  @PrePersist
  protected void onCreate() {
    if (processedAt == null) {
      processedAt = Instant.now();
    }
  }

  public InboxEvent(String eventId, Long aggregateId, String eventType) {
    this.eventId = eventId;
    this.aggregateId = aggregateId;
    this.eventType = eventType;
  }
}
