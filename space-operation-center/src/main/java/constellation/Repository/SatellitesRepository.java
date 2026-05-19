package constellation.Repository;

import constellation.Model.Domain.Satellite.Satellite;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SatellitesRepository extends JpaRepository<Satellite, Long> {

  Optional<Satellite> findByName(String name);

  @Modifying
  @Transactional
  @Query("UPDATE Satellite s SET s.outsideTemperature = :outsideTemp, s.insideTemperature = :insideTemp WHERE s.id = :satelliteId")
  int updateTemperatures(@Param("satelliteId") Long satelliteId,
      @Param("outsideTemp") Double outsideTemperature,
      @Param("insideTemp") Double insideTemperature);
}
