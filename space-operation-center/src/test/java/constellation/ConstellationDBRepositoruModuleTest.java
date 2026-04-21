package constellation;


import static org.assertj.core.api.Assertions.assertThat;

import constellation.Model.Domain.Satellite.SatelliteParam.CommunicationSatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import constellation.Model.Factory.SatelliteFactory.Impl.CommunicationSatelliteFactory;
import constellation.Repository.DBRepository.ConstellationsRepository;
import constellation.Service.ConstellationService.ServiceDB.ConstellationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@DataJpaTest
@Import(ConstellationService.class)
public class ConstellationDBRepositoruModuleTest {

  @Autowired
  private ConstellationsRepository constellationRepository;

  @Autowired
  private ConstellationService constellationService;

  @Test
  void shouldCreateAndFindConstellation() {
    // Given
    String name = "Test-Group";

    // When
    constellationService.createAndSaveConstellation(name);
    var found = constellationRepository.findByConstellationName(name);

    // Then
    assertThat(found).isPresent();
    assertThat(found.get().getConstellationName()).isEqualTo(name);
//    assertThat(saved.getId()).isNotNull();
  }


  @Test
  void shouldAddSatelliteToConstellation() {
    // Given
    constellationService.createAndSaveConstellation("Orbit-1");
    var factory = new CommunicationSatelliteFactory();
    var satellite = factory.createSatelliteWithParameter(
        new CommunicationSatelliteParam(SatelliteType.COMMUNICATION, "COMM", 80,
            500));

    // When
    constellationService.addSatelliteToConstellation("Orbit-1", satellite);

    // Then
    var satellites = constellationRepository.findByConstellationName("Orbit-1")
        .orElseThrow()
        .getSatellites();
    assertThat(satellites).hasSize(1);
    assertThat(satellites.get(0).getName()).isEqualTo("COMM");
  }
}
