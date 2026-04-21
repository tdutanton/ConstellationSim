package constellation.Repository.DBRepository;

import constellation.Model.Domain.Satellite.Satellite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SatellitesRepository extends JpaRepository<Satellite, Long> {

  List<Satellite> findByConstellation_ConstellationName(String constellationName);

  List<Satellite> findByIsActiveTrue();

  Optional<Satellite> findBySatelliteName(String name);
}
