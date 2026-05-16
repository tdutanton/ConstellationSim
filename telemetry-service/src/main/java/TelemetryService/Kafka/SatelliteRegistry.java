package TelemetryService.Kafka;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SatelliteRegistry {

  // ✅ ConcurrentHashMap.newKeySet() — потокобезопасный Set
  private final Set<Long> activeSatelliteIds = ConcurrentHashMap.newKeySet();

  public void addSatellite(Long satelliteId) {
    activeSatelliteIds.add(satelliteId);
  }

  public void removeSatellite(Long satelliteId) {
    activeSatelliteIds.remove(satelliteId);
  }

  public Set<Long> getActiveIds() {
    return Set.copyOf(activeSatelliteIds);
  }

  public boolean contains(Long satelliteId) {
    return activeSatelliteIds.contains(satelliteId);
  }

  public int size() {
    return activeSatelliteIds.size();
  }
}
