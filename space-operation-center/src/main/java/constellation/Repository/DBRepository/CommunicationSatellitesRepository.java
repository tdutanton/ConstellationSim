package constellation.Repository.DBRepository;

import constellation.Model.Domain.Satellite.CommunicationSatellite;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunicationSatellitesRepository extends JpaRepository<CommunicationSatellite, Long> {
  Optional<CommunicationSatellite> findByName(String name);
}