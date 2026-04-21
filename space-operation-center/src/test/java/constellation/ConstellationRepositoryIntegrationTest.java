package constellation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Model.Domain.Exception.SpaceOperationException;
import constellation.Model.Domain.Satellite.CommunicationSatellite;
import constellation.Model.Domain.Satellite.ImagingSatellite;
import constellation.Model.Domain.Satellite.Satellite;
import constellation.Model.Domain.Satellite.SatelliteParam.CommunicationSatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteParam;
import constellation.Model.Domain.Satellite.SatelliteParam.SatelliteType;
import constellation.Model.Factory.SatelliteFactory.Impl.CommunicationSatelliteFactory;
import constellation.Model.Factory.SatelliteFactory.Impl.ImagingSatelliteFactory;
import constellation.Model.Factory.SatelliteFactory.SatelliteFactory;
import constellation.Repository.RunTimeRepository.ConstellationRepository;
import constellation.Service.SatelliteService.Impl.SatelliteServiceImpl;
import constellation.Service.SatelliteService.SatelliteService;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("ConstellationRepository: интеграционные тесты")
class ConstellationRepositoryIntegrationTest {

  @Autowired
  private ConstellationRepository repository;

  private String constellationName;
  private String secondConstellationName;

  @BeforeEach
  void setUp() {
    constellationName = "Орбита-" + UUID.randomUUID().toString().substring(0, 4);
    secondConstellationName = "Орбита-" + UUID.randomUUID().toString().substring(0, 4);
  }

  @Test
  @DisplayName("Проверка работы SatelliteServiceImpl")
  void shouldCreateCorrectSatellite() {
    SatelliteParam param = new CommunicationSatelliteParam(SatelliteType.COMMUNICATION, "Com", 55,
        400);
    SatelliteFactory comFactory = new CommunicationSatelliteFactory();
    SatelliteFactory imgFactory = new ImagingSatelliteFactory();
    ArrayList<SatelliteFactory> list = new ArrayList<>();
    list.add(comFactory);
    list.add(imgFactory);
    SatelliteService service = new SatelliteServiceImpl(list);
    try {
      Satellite res = service.createSatellite(param);
      assertEquals("Com-1", res.getName());
      assertEquals(CommunicationSatellite.class, res.getClass());
    } catch (SpaceOperationException e) {
      System.out.println(e.getMessage());
    }
  }

  @Nested
  @DisplayName("Полный жизненный цикл группировки")
  class FullLifecycle {

    @Test
    @DisplayName("создание - добавление спутников - активация - выполнение миссий - деактивация")
    void shouldCompleteFullLifecycleSuccessfully() {
      SatelliteConstellation constellation = new SatelliteConstellation.ConstellationBuilder()
          .setConstellationName(
              constellationName).build();
      repository.addConstellation(constellation);

      SatelliteConstellation retrieved = repository.constellationByName(constellationName);
      assertNotNull(retrieved, "Группировка должна существовать");
      assertEquals(0, retrieved.getSatellites().size(), "Спутников ещё нет");

      CommunicationSatellite commSatellite = new CommunicationSatellite(500.0);
      ImagingSatellite imagingSatellite = new ImagingSatellite(1.5);

      repository.addSatellite(constellationName, commSatellite);
      repository.addSatellite(constellationName, imagingSatellite);

      assertEquals(2, retrieved.getSatellites().size(), "Должно быть 2 спутника");
      assertFalse(commSatellite.getState().isActive(),
          "Спутник должен быть неактивен после создания");

      if (commSatellite.activate()) {
        assertTrue(commSatellite.getState().isActive(), "Активация должна быть успешной");
      }
      if (imagingSatellite.activate()) {
        assertTrue(imagingSatellite.getState().isActive(), "Активация должна быть успешной");
      }

      double chargeBefore = commSatellite.getEnergy().getBatteryLevel();
      commSatellite.performMission();
      double chargeAfter = commSatellite.getEnergy().getBatteryLevel();

      if (commSatellite.getState().isActive()) {
        assertTrue(chargeAfter < chargeBefore, "Заряд должен уменьшиться после миссии");
        assertTrue(commSatellite.getState().isActive(), "Спутник должен остаться активным");
      }

      while (commSatellite.getState().isActive()
          && commSatellite.getEnergy().getBatteryLevel() > 0.01) {
        commSatellite.performMission();
      }

      assertFalse(commSatellite.getState().isActive(),
          "Спутник должен деактивироваться при низком заряде");

      repository.deleteSatellite(constellationName, imagingSatellite);
      assertFalse(retrieved.containsSatellite(imagingSatellite), "Спутник должен быть удалён");
      assertEquals(1, retrieved.getSatellites().size(), "Должен остаться 1 спутник");

      repository.removeConstellation(constellation);
      assertNull(repository.constellationByName(constellationName),
          "Группировка должна быть удалена");
    }
  }

  @Nested
  @DisplayName("Работа с несколькими группировками")
  class MultipleConstellations {

    @Test
    @DisplayName("успешное управление несколькими независимыми группировками")
    void shouldManageMultipleIndependentConstellations() {
      SatelliteConstellation first = new SatelliteConstellation.ConstellationBuilder()
          .setConstellationName(
              constellationName).build();
      SatelliteConstellation second = new SatelliteConstellation.ConstellationBuilder()
          .setConstellationName(
              secondConstellationName).build();

      repository.addConstellation(first);
      repository.addConstellation(second);

      CommunicationSatellite sat1 = new CommunicationSatellite(500.0);
      CommunicationSatellite sat2 = new CommunicationSatellite(1000.0);

      repository.addSatellite(constellationName, sat1);
      repository.addSatellite(secondConstellationName, sat2);

      SatelliteConstellation firstRetrieved = repository.constellationByName(constellationName);
      SatelliteConstellation secondRetrieved = repository.constellationByName(
          secondConstellationName);

      assertNotNull(firstRetrieved, "Первая группировка должна существовать");
      assertNotNull(secondRetrieved, "Вторая группировка должна существовать");

      assertEquals(1, firstRetrieved.getSatellites().size(),
          "В первой группировке должен быть 1 спутник");
      assertEquals(1, secondRetrieved.getSatellites().size(),
          "Во второй группировке должен быть 1 спутник");

      sat1.activate();
      assertTrue(sat1.getState().isActive(), "Спутник в первой группировке должен быть активен");
      assertFalse(sat2.getState().isActive(),
          "Спутник во второй группировке должен оставаться неактивным");
    }
  }

  @Nested
  @DisplayName("Граничные случаи")
  class EdgeCases {

    @Test
    @DisplayName("игнорирование операций с несуществующими группировками без исключений")
    void shouldHandleNonExistentConstellationsGracefully() {
      CommunicationSatellite satellite = new CommunicationSatellite(300.0);

      assertDoesNotThrow(() ->
          repository.addSatellite("НЕ_СУЩЕСТВУЕТ", satellite)
      );

      assertDoesNotThrow(() ->
          repository.deleteSatellite("НЕ_СУЩЕСТВУЕТ", satellite)
      );

      assertNull(repository.constellationByName("НЕ_СУЩЕСТВУЕТ"));
    }

    @Test
    @DisplayName("корректная обработка дубликатов группировок")
    void shouldIgnoreDuplicateConstellationAddition() {
      SatelliteConstellation first = new SatelliteConstellation.ConstellationBuilder()
          .setConstellationName(
              constellationName).build();
      SatelliteConstellation duplicate = new SatelliteConstellation.ConstellationBuilder()
          .setConstellationName(
              constellationName).build();

      repository.addConstellation(first);
      repository.addConstellation(duplicate);
      SatelliteConstellation retrieved = repository.constellationByName(constellationName);
      assertSame(first, retrieved, "Должна остаться оригинальная группировка");
    }
  }

  @Nested
  @DisplayName("Проверка состояний спутников")
  class SatelliteStateTransitions {

    @Test
    @DisplayName("естественная деактивация при разряде через выполнение миссий")
    void shouldDeactivateNaturallyAfterMultipleMissions() {
      SatelliteConstellation constellation = new SatelliteConstellation.ConstellationBuilder()
          .setConstellationName(
              constellationName).build();
      repository.addConstellation(constellation);

      CommunicationSatellite satellite = new CommunicationSatellite(500.0);
      repository.addSatellite(constellationName, satellite);
      satellite.activate();

      int missionCount = 0;
      while (satellite.getState().isActive() && missionCount < 100) {
        satellite.performMission();
        missionCount++;
      }

      assertFalse(satellite.getState().isActive(),
          "Спутник должен деактивироваться после " + missionCount + " миссий");
    }
  }
}