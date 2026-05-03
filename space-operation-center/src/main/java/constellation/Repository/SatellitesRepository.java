package constellation.Repository;

import constellation.Model.Domain.Satellite.Satellite;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SatellitesRepository extends JpaRepository<Satellite, Long> {

  Optional<Satellite> findByName(String name);
}
