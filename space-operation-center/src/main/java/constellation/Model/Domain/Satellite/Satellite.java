package constellation.Model.Domain.Satellite;

import constellation.Model.Domain.Constellation.SatelliteConstellation;
import constellation.Model.Domain.Internal.EnergySystem.EnergySystem;
import constellation.Model.Domain.Internal.SatelliteState.SatelliteState;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Абстрактный базовый класс для представления спутника. Содержит общую логику управления состоянием
 * спутника: активация/деактивация, контроль уровня заряда батареи и выполнение миссий. Потомки
 * должны реализовать метод {@link #performMission()}.
 */
@EqualsAndHashCode
@Entity
@Table(name = "satellites")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "satellite_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
public abstract class Satellite {

  /**
   * Минимальный допустимый уровень заряда для активации спутника (20%).
   */
  private static final double MIN_POSSIBLE_BATTERY_FOR_ACTIVATE = 0.20;
  /**
   * Флаг, включающий или отключающий подробный вывод в консоль при создании, активации и выполнении
   * операций со спутником.
   */
  @Setter
  protected static boolean consolePrintMode = false;
  /**
   * Имя спутника, генерируется автоматически при создании.
   */
  @Getter
  @Column(nullable = false, unique = true)
  protected String name;
  /**
   * Текущее состояние спутника (активен/неактивен).
   */
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "isActive", column = @Column(name = "is_active"))
  })
  protected SatelliteState state;
  /**
   * Система управления энергией спутника.
   */
  @Getter
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "batteryLevel", column = @Column(name = "battery_level"))
  })
  protected EnergySystem energy;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "constellation_id")
  private SatelliteConstellation constellation;

  /**
   * Конструктор спутника. Генерирует уникальное имя на основе переданного префикса и номера,
   * инициализирует начальный уровень заряда батареи случайным значением, устанавливает состояние
   * как неактивное. При включённом {@code consolePrintMode} выводит сообщение о создании спутника.
   *
   * @param aName префикс имени спутника (например, "Связь" или "ДЗЗ")
   */
  protected Satellite(String aName) {
    name = generateName(aName);
    state = new SatelliteState();
    this.energy = new EnergySystem.EnergySystemBuilder().build();
    System.out.printf("\uD83D\uDEF0\uFE0F Создан спутник: %s (заряд: %.0f%%)%n", name,
        energy.getBatteryLevel() * 100.0);
  }

  protected Satellite(String aName, double batteryLevel) {
    try {
      name = generateName(aName);
      state = new SatelliteState();
      this.energy = new EnergySystem.EnergySystemBuilder().setBatteryLevel(batteryLevel).build();
      System.out.printf("\uD83D\uDEF0\uFE0F Создан спутник: %s (заряд: %.0f%%)%n", name,
          energy.getBatteryLevel() * 100.0);
    } catch (IllegalArgumentException e) {
      System.out.println("Ошибка при создании спутника: " + e);
    }
  }

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  /**
   * Генерирует уникальное имя спутника на основе префикса и номера.
   *
   * @param name префикс имени
   * @return сформированное имя вида "префикс-номер"
   */
  private String generateName(String name) {
    return name;
  }

  /**
   * Пытается активировать спутник.
   * <p>
   * Активация возможна только если:
   * <ul>
   *   <li>уровень заряда батареи превышает {@link #MIN_POSSIBLE_BATTERY_FOR_ACTIVATE}, и</li>
   *   <li>спутник в данный момент неактивен.</li>
   * </ul>
   * Если спутник уже активен, операция считается успешной, но повторной активации не происходит.
   * При недостаточном заряде активация отклоняется.
   * </p>
   *
   * @return {@code true}, если активация прошла успешно (включая случай, когда спутник уже был
   * активен), {@code false} — если активация невозможна из-за низкого заряда батареи
   */
  public boolean activate() {
    if (energy.getBatteryLevel() > MIN_POSSIBLE_BATTERY_FOR_ACTIVATE && !state.isActive()) {
      state.activate();
      System.out.printf("✅ %s: Активация успешна%n", name);
      return true;
    } else if (state.isActive()) {
      System.out.printf("⚠️ %s: Активация уже была произведена ранее%n", name);
      return true;
    } else {
      System.out.printf("⛔ %s: Ошибка активации (заряд: %.0f%%)%n", name,
          energy.getBatteryLevel() * 100);
      return false;
    }
  }

  /**
   * Проверяет уровень заряда и деактивирует спутник, если он опустился до или ниже порогового
   * значения {@link #MIN_POSSIBLE_BATTERY_FOR_ACTIVATE}. Вызывается автоматически после операций,
   * расходующих заряд.
   */
  protected void handleChangeBatteryLevel() {
    if (energy.getBatteryLevel() <= MIN_POSSIBLE_BATTERY_FOR_ACTIVATE && state.isActive()) {
      state.deactivate();
    }
  }

  /**
   * Выполняет основную миссию спутника. Конкретная реализация зависит от типа спутника и должна
   * быть предоставлена в наследующих классах (например, передача данных или съёмка).
   */
  protected abstract void performMission();

  public void executeMission() {
    performMission();
  }

  /**
   * Получить состояние спутника
   *
   * @return экземпляр класса SatelliteState - состояние
   */
  public SatelliteState getState() {
    return state;
  }

  /**
   * Получить строковый вид статуса спутника
   *
   * @return String - статус спутника в виде строки
   */
  protected String status() {
    return state.toString();
  }
}