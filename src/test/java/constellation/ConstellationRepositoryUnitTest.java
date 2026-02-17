package constellation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import constellation.Domain.Constellation.SatelliteConstellation;
import constellation.Domain.Internal.EnergySystem.EnergySystem;
import constellation.Domain.Satellite.ImagingSatellite;
import constellation.Domain.Satellite.Satellite;
import constellation.Domain.SatelliteFactory.CommunicationSatelliteFactory;
import constellation.Domain.SatelliteFactory.SatelliteFactory;
import constellation.Repository.ConstellationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Юнит-тесты для репозитория спутниковых группировок. Проверяет все публичные методы без
 * использования внешних зависимостей.
 */
@DisplayName("ConstellationRepository: юнит-тесты")
public class ConstellationRepositoryUnitTest {

  // Общие константы для тестовых данных
  private static final String FIRST_CONSTELLATION = "Орбита-1";
  private static final String SECOND_CONSTELLATION = "Орбита-2";

  private ConstellationRepository repository;
  private SatelliteConstellation fConstellation;
  private SatelliteConstellation sConstellation;
  private Satellite commSatellite;
  private ImagingSatellite imagingSatellite;
  private SatelliteFactory factory = new CommunicationSatelliteFactory();

  @BeforeEach
  void setUp() {
    repository = new ConstellationRepository();
    fConstellation = new SatelliteConstellation(FIRST_CONSTELLATION);
    sConstellation = new SatelliteConstellation(SECOND_CONSTELLATION);
    commSatellite = factory.createSatelliteWithParameter("Связь", 65, 200);
    imagingSatellite = new ImagingSatellite(1.0);
  }

  @Nested
  @DisplayName("Добавление группировки")
  class AddingConstellation {

    @Test
    @DisplayName("успешно добавляет новую группировку")
    void shouldAddNewConstellation() {
      repository.addConstellation(fConstellation);

      SatelliteConstellation retrieved = repository.constellationByName(FIRST_CONSTELLATION);
      assertNotNull(retrieved, "Группировка должна существовать");
      assertEquals(FIRST_CONSTELLATION, retrieved.getConstellationName());
    }

    @Test
    @DisplayName("игнорирует дубликат группировки")
    void shouldIgnoreDuplicateConstellation() {
      repository.addConstellation(fConstellation);

      SatelliteConstellation duplicate = new SatelliteConstellation(FIRST_CONSTELLATION);
      repository.addConstellation(duplicate);

      SatelliteConstellation retrieved = repository.constellationByName(FIRST_CONSTELLATION);
      assertSame(fConstellation, retrieved, "Должна остаться оригинальная группировка");
    }
  }

  @Nested
  @DisplayName("Удаление группировки")
  class DeletingConstellation {

    @Test
    @DisplayName("успешно удаляет существующую группировку")
    void shouldDeleteExistingConstellation() {
      repository.addConstellation(fConstellation);
      assertNotNull(repository.constellationByName(FIRST_CONSTELLATION));
      repository.removeConstellation(fConstellation);
      assertNull(repository.constellationByName(FIRST_CONSTELLATION));
      assertFalse(repository.containsConstellations(),
          "Репозиторий должен быть пустым после удаления");
    }

    @Test
    @DisplayName("игнорирует удаление несуществующей группировки")
    void shouldIgnoreDeletingNonExistentConstellation() {
      SatelliteConstellation nonExistent = new SatelliteConstellation("Несуществующая");
      assertDoesNotThrow(() ->
          repository.removeConstellation(nonExistent)
      );
      assertFalse(repository.containsConstellations(), "Репозиторий должен оставаться пустым");
    }
  }


  @Nested
  @DisplayName("Добавление спутника")
  class AddingSatellite {

    @Test
    @DisplayName("успешно добавляет спутник в существующую группировку")
    void shouldAddSatelliteToExistingConstellation() {
      repository.addConstellation(fConstellation);
      repository.addSatellite(FIRST_CONSTELLATION, commSatellite);

      SatelliteConstellation retrieved = repository.constellationByName(FIRST_CONSTELLATION);
      assertTrue(retrieved.containsSatellite(commSatellite));
      assertEquals(1, retrieved.getSatellites().size());
    }

    @Test
    @DisplayName("игнорирует добавление в несуществующую группировку")
    void shouldIgnoreAddingToNonExistentConstellation() {
      assertDoesNotThrow(() ->
          repository.addSatellite(FIRST_CONSTELLATION, commSatellite)
      );
      assertNull(repository.constellationByName(FIRST_CONSTELLATION));
    }
  }

  @Nested
  @DisplayName("Удаление спутника")
  class DeletingSatellite {

    @Test
    @DisplayName("успешно удаляет спутник из группировки")
    void shouldDeleteExistingSatellite() {
      repository.addConstellation(fConstellation);
      repository.addSatellite(FIRST_CONSTELLATION, commSatellite);
      repository.deleteSatellite(FIRST_CONSTELLATION, commSatellite);

      SatelliteConstellation retrieved = repository.constellationByName(FIRST_CONSTELLATION);
      assertFalse(retrieved.containsSatellite(commSatellite));
      assertEquals(0, retrieved.getSatellites().size());
    }

    @Test
    @DisplayName("игнорирует удаление из несуществующей группировки")
    void shouldIgnoreDeletingFromNonExistentConstellation() {
      assertDoesNotThrow(() ->
          repository.deleteSatellite(FIRST_CONSTELLATION, commSatellite)
      );
    }
  }

  @Nested
  @DisplayName("Получение группировки по имени")
  class GettingConstellationByName {

    @Test
    @DisplayName("возвращает существующую группировку")
    void shouldReturnExistingConstellation() {
      repository.addConstellation(fConstellation);
      assertNotNull(repository.constellationByName(FIRST_CONSTELLATION));
    }

    @Test
    @DisplayName("возвращает null для несуществующей группировки")
    void shouldReturnNullForNonExistentConstellation() {
      assertNull(repository.constellationByName(FIRST_CONSTELLATION));
    }

    @Test
    @DisplayName("выбрасывает NullPointerException при null-имени")
    void shouldThrowNpeForNullName() {
      assertThrows(NullPointerException.class, () ->
          repository.constellationByName(null)
      );
    }
  }

  @Nested
  @DisplayName("Граничные случаи")
  class EdgeCases {

    @Test
    @DisplayName("обрабатывает группировку с пустым именем")
    void shouldHandleEmptyName() {
      SatelliteConstellation empty = new SatelliteConstellation("");
      repository.addConstellation(empty);
      assertNotNull(repository.constellationByName(""));
    }

    @Test
    @DisplayName("обрабатывает очень длинное имя группировки")
    void shouldHandleVeryLongName() {
      String longName = "A".repeat(1000);
      SatelliteConstellation longConst = new SatelliteConstellation(longName);
      repository.addConstellation(longConst);
      assertNotNull(repository.constellationByName(longName));
    }
  }

  @Test
  @DisplayName("Бросает IllegalArgumentException при отрицательном заряде батареи")
  void shouldThrowExceptionForNegativeBatteryLevel() {
    assertThrows(IllegalArgumentException.class, () -> {
      new EnergySystem(-100);
    });
  }

  @Test
  @DisplayName("Бросает IllegalArgumentException при большом заряде батареи")
  void shouldThrowExceptionForBigBatteryLevel() {
    assertThrows(IllegalArgumentException.class, () -> {
      new EnergySystem(110);
    });
  }

  @Test
  @DisplayName("отрабатывает корректный аргумент в заряде батареи")
  void shouldGetCorrectBatteryLevel() {
    EnergySystem system = new EnergySystem(55);
    assertEquals(0.55, system.getBatteryLevel());
  }
}
