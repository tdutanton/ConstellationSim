package constellation.Model.Domain.Constellation;

import constellation.Model.Domain.Internal.SatelliteState.SatelliteState;
import constellation.Model.Domain.Satellite.Satellite;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс для управления спутниковой группировкой.
 * <p>
 * Представляет собой коллекцию спутников ({@link Satellite}), позволяя добавлять спутники,
 * активировать их и запускать выполнение миссий для всех членов группировки одновременно.
 * </p>
 */
@Entity
@Table(name = "constellations")
@Getter
@Setter
public class SatelliteConstellation {

  /**
   * Строка-разделитель, используемая при выводе в консоль.
   */
  private static final String NEW_LINE_DELIM_CONSTELLATION = "---------------------";
  /**
   * Флаг, включающий или отключающий подробный вывод в консоль при создании, модификации и
   * управлении группировкой.
   */
  @Setter
  private static boolean consolePrintMode = false;
  /**
   * Название спутниковой группировки.
   */
  @Column(nullable = false, unique = true)
  @EqualsAndHashCode.Include
  private final String constellationName;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;
  /**
   * Список спутников, входящих в состав группировки.
   */
  @OneToMany(mappedBy = "constellation", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Satellite> satellites = new ArrayList<>();

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;


  /**
   * Конструктор, вызываемый при помощи строителя
   *
   */
  private SatelliteConstellation(ConstellationBuilder builder) {
    this.constellationName = builder.constellationName;
    this.satellites = builder.satellites != null ? builder.satellites : new ArrayList<>();
  }

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
    updatedAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Instant.now();
  }

  /**
   * Выводит сообщение о создании группировки, если включён режим {@link #consolePrintMode}.
   */
  private void printConstellationCtor() {
    if (consolePrintMode) {
      System.out.printf("Создана спутниковая группировка: %s%n", constellationName);
    }
  }

  /**
   * Добавляет спутник в состав группировки.
   * <p>
   * При включённом {@code consolePrintMode} выводится подтверждающее сообщение.
   * </p>
   *
   * @param satellite спутник для добавления (не должен быть {@code null})
   */
  public void addSatellite(Satellite satellite) {
    satellites.add(satellite);
    satellite.setConstellation(this);
    if (consolePrintMode) {
      System.out.printf("%s добавлен в группировку '%s'%n", satellite.getName(), constellationName);
    }
  }

  /**
   * Удаляет спутник из группировки.
   * <p>
   * При включённом {@code consolePrintMode} выводится подтверждающее сообщение.
   * </p>
   *
   * @param satellite спутник для удаления (не должен быть {@code null})
   */
  public void deleteSatellite(Satellite satellite) {
    satellites.remove(satellite);
    satellite.setConstellation(null);
    if (consolePrintMode) {
      System.out.printf("%s удален из группировки '%s'%n", satellite.getName(), constellationName);
    }
  }

  /**
   * Запускает выполнение миссий для всех спутников в группировке.
   * <p>
   * Вызывает метод {@link Satellite#executeMission()} у каждого спутника. При включённом
   * {@code consolePrintMode} выводится заголовок операции.
   * </p>
   */
  public void executeAllMissions() {
    if (consolePrintMode) {
      System.out.printf("Выполнение миссий группировки %s%n", constellationName);
      System.out.println(NEW_LINE_DELIM_CONSTELLATION);
    }
    for (Satellite s : satellites) {
      s.executeMission();
    }
  }

  /**
   * Активирует все спутники в группировке.
   * <p>
   * Вызывает метод {@link Satellite#activate()} для каждого спутника. Если группировка пуста,
   * выводится соответствующее сообщение (при включённом {@code consolePrintMode}).
   * </p>
   */
  public void activateSatellites() {
    if (consolePrintMode) {
      System.out.println("АКТИВАЦИЯ СПУТНИКОВ");
      System.out.println(NEW_LINE_DELIM_CONSTELLATION);
    }
    if (!satellites.isEmpty()) {
      satellites.forEach(Satellite::activate);
    } else {
      if (consolePrintMode) {
        System.out.printf("⛔ Активация спутников невозможна. В группировке %s отсутствуют спутники",
            constellationName);
      }
    }
    if (consolePrintMode) {
      System.out.println(NEW_LINE_DELIM_CONSTELLATION);
    }
  }

  /**
   * Возвращает список текущих состояний всех спутников в группировке.
   * <p>
   * Для каждого спутника вызывается метод {@link Satellite#getState()}, и результаты собираются в
   * новый список типа {@link SatelliteState}. Порядок состояний в списке соответствует порядку
   * спутников в группировке.
   * </p>
   *
   * @return список объектов {@link SatelliteState}, описывающих состояние каждого спутника
   */
  public List<SatelliteState> getSatellitesStatus() {
    List<SatelliteState> result = new ArrayList<>();
    for (Satellite s : satellites) {
      result.add(s.getState());
    }
    return result;
  }

  /**
   * Возвращает строковое представление спутниковой группировки.
   * <p>
   * Формат строки: <code>{название=SatelliteConstellation{constellationName=...,
   * satellites=...}}</code>, где содержимое поля {@code satellites} выводится через стандартный
   * метод {@code toString()} списка.
   * </p>
   *
   * @return строковое представление объекта группировки
   */
  @Override
  public String toString() {
    return String.format(
        "{%s=%s{constellationName=%s, satellites=%s}}",
        getConstellationName(),
        this.getClass().getSimpleName(),
        getConstellationName(),
        satellites
    );
  }

  public boolean containsSatellite(Satellite satellite) {
    return satellites.contains(satellite);
  }

  public Satellite satelliteByName(String satelliteName) {
    if (satelliteName == null || satelliteName.trim().isEmpty()) {
      return null;
    }

    return satellites.stream()
        .filter(satellite -> satellite != null && satellite.getName() != null)
        .filter(satellite -> satellite.getName().equals(satelliteName))
        .findFirst()
        .orElse(null);
  }

  /**
   * Внутренний статичный класс для создания экземпляров Constellation при помощи паттерна
   * Строитель
   */
  public static class ConstellationBuilder {

    /**
     * Название группировки по умолчанию, используемое при вызове конструктора без параметров.
     */
    private static final String DEFAULT_CONSTELLATION_NAME = "Default Constellation";

    /**
     * Название спутниковой группировки.
     */
    private String constellationName = DEFAULT_CONSTELLATION_NAME;

    /**
     * Список спутников, входящих в состав группировки.
     */
    private List<Satellite> satellites;

    public ConstellationBuilder setConstellationName(String name) {
      this.constellationName = name;
      return this;
    }

    public ConstellationBuilder addSatellite(Satellite satellite) {
      if (this.satellites.isEmpty()) {
        this.satellites = new ArrayList<>();
      }
      satellites.add(satellite);
      return this;
    }

    public ConstellationBuilder addListSatellites(List<Satellite> satellites) {
      if (this.satellites.isEmpty()) {
        this.satellites = new ArrayList<>();
      }
      this.satellites.addAll(satellites);
      return this;
    }

    public SatelliteConstellation build() {
      SatelliteConstellation result = new SatelliteConstellation(this);
      result.printConstellationCtor();
      return result;
    }
  }
}