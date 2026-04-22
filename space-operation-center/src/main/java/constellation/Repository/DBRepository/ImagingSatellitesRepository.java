package constellation.Repository.DBRepository;

import constellation.Model.Domain.Satellite.ImagingSatellite;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagingSatellitesRepository extends JpaRepository<ImagingSatellite, Long> {
  Optional<ImagingSatellite> findByName(String name);
}