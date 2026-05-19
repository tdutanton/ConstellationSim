package TelemetryService.Kafka;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SatelliteRegistry {

  // потокобезопасный Set для хранения ID активных спутников
  private final Set<Long> activeSatelliteIds = ConcurrentHashMap.newKeySet();

  // добавить
  public void addSatellite(Long satelliteId) {
    activeSatelliteIds.add(satelliteId);
  }

  //удалить
  public void removeSatellite(Long satelliteId) {
    activeSatelliteIds.remove(satelliteId);
  }

  // получить список текущих id
  public Set<Long> getActiveIds() {
    return Set.copyOf(activeSatelliteIds);
  }

  // проверка есть ли в списке
  public boolean contains(Long satelliteId) {
    return activeSatelliteIds.contains(satelliteId);
  }

  // количество элементов
  public int size() {
    return activeSatelliteIds.size();
  }
}
