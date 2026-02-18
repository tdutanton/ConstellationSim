package constellation;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import constellation.Domain.Constellation.SatelliteConstellation;
import constellation.Domain.Satellite.Satellite;
import constellation.Repository.ConstellationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConstellationRepository: мок-тесты")
class ConstellationRepositoryMockTest {

  private static final String CONSTELLATION_NAME = "Орбита-1";
  private static final String SATELLITE_NAME = "Связь-1";

  @Mock
  private SatelliteConstellation mockConstellation;

  @Mock
  private Satellite mockSatellite;

  private ConstellationRepository repository;

  @BeforeEach
  void setUp() {
    repository = new ConstellationRepository();
  }

  @Nested
  @DisplayName("Взаимодействие с группировкой при добавлении спутника")
  class InteractionWhenAddingSatellite {

    @Test
    @DisplayName("вызывает addSatellite() у группировки при добавлении спутника")
    void shouldCallAddSatelliteOnConstellation() {
      when(mockConstellation.getConstellationName()).thenReturn(CONSTELLATION_NAME);
      when(mockSatellite.getName()).thenReturn(SATELLITE_NAME);

      repository.addConstellation(mockConstellation);
      repository.addSatellite(CONSTELLATION_NAME, mockSatellite);
      verify(mockConstellation).addSatellite(mockSatellite);
      verify(mockConstellation, never()).deleteSatellite(any());
    }

    @Test
    @DisplayName("НЕ вызывает addSatellite() у несуществующей группировки")
    void shouldNotCallAddSatelliteOnNonExistentConstellation() {
      repository.addSatellite("Не_существует", mockSatellite);

      verify(mockConstellation, never()).addSatellite(any());
    }
  }

  @Nested
  @DisplayName("Взаимодействие с группировкой при удалении спутника")
  class InteractionWhenDeletingSatellite {

    @Test
    @DisplayName("вызывает deleteSatellite() у группировки при удалении спутника")
    void shouldCallDeleteSatelliteOnConstellation() {
      when(mockConstellation.getConstellationName()).thenReturn(CONSTELLATION_NAME);
      when(mockSatellite.getName()).thenReturn(SATELLITE_NAME);

      repository.addConstellation(mockConstellation);
      repository.deleteSatellite(CONSTELLATION_NAME, mockSatellite);

      verify(mockConstellation).deleteSatellite(mockSatellite);
      verify(mockConstellation, never()).addSatellite(any());
    }

    @Test
    @DisplayName("НЕ вызывает deleteSatellite() у несуществующей группировки")
    void shouldNotCallDeleteSatelliteOnNonExistentConstellation() {
      repository.deleteSatellite("Не_существует", mockSatellite);
      verify(mockConstellation, never()).deleteSatellite(any());
    }
  }

  @Nested
  @DisplayName("Взаимодействие при работе с группировками")
  class InteractionWithConstellations {

    @Test
    @DisplayName("сохраняет мок-группировку в хранилище")
    void shouldStoreMockConstellation() {
      when(mockConstellation.getConstellationName()).thenReturn(CONSTELLATION_NAME);

      repository.addConstellation(mockConstellation);

      SatelliteConstellation retrieved = repository.constellationByName(CONSTELLATION_NAME);
      assertSame(mockConstellation, retrieved, "Репозиторий должен хранить мок-объект");
    }

    @Test
    @DisplayName("игнорирует добавление дубликата мок-группировки")
    void shouldIgnoreDuplicateMockConstellation() {
      when(mockConstellation.getConstellationName()).thenReturn(CONSTELLATION_NAME);

      repository.addConstellation(mockConstellation);

      SatelliteConstellation duplicateMock = mock(SatelliteConstellation.class);
      when(duplicateMock.getConstellationName()).thenReturn(CONSTELLATION_NAME);

      repository.addConstellation(duplicateMock);

      SatelliteConstellation retrieved = repository.constellationByName(CONSTELLATION_NAME);
      assertSame(mockConstellation, retrieved, "Должен остаться первый добавленный мок");

      verify(duplicateMock, never()).addSatellite(any());
      verify(duplicateMock, never()).deleteSatellite(any());
    }
  }

  @Nested
  @DisplayName("Проверка аргументов вызовов")
  class ArgumentVerification {

    @Test
    @DisplayName("передаёт правильный спутник в addSatellite() группировки")
    void shouldPassCorrectSatelliteToAddSatellite() {
      when(mockConstellation.getConstellationName()).thenReturn(CONSTELLATION_NAME);
      when(mockSatellite.getName()).thenReturn(SATELLITE_NAME);

      repository.addConstellation(mockConstellation);
      repository.addSatellite(CONSTELLATION_NAME, mockSatellite);

      verify(mockConstellation).addSatellite(argThat(sat ->
          sat.getName().equals(SATELLITE_NAME)
      ));
    }

    @Test
    @DisplayName("передаёт правильный спутник в deleteSatellite() группировки")
    void shouldPassCorrectSatelliteToDeleteSatellite() {
      when(mockConstellation.getConstellationName()).thenReturn(CONSTELLATION_NAME);
      when(mockSatellite.getName()).thenReturn(SATELLITE_NAME);

      repository.addConstellation(mockConstellation);
      repository.deleteSatellite(CONSTELLATION_NAME, mockSatellite);

      verify(mockConstellation).deleteSatellite(argThat(sat ->
          sat.getName().equals(SATELLITE_NAME)
      ));
    }
  }
}