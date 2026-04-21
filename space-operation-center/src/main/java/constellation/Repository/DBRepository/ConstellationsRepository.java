package constellation.Repository.DBRepository;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConstellationsRepository extends JpaRepository<SatelliteConstellation, Long> {

  Optional<SatelliteConstellation> findByConstellationName(String name);

  boolean existsByConstellationName(String name);
}
