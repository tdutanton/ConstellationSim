package constellation.Kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SatelliteEventPayload {

  private String eventId;
  private String type;
  private Long satelliteId;
}
