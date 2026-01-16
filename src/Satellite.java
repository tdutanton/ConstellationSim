/**
 * Абстрактный базовый класс, представляющий спутник.
 * <p>
 * Спутник обладает именем, состоянием (активен/неактивен), системой энергоснабжения
 * и способностью активироваться или деактивироваться в зависимости от уровня заряда батареи.
 * Подклассы обязаны реализовать метод {@link #performMission()}, определяющий
 * конкретную миссию спутника.
 * </p>
 */
public abstract class Satellite {
    /** Имя спутника, генерируемое на основе базового имени и номера. */
    protected String name;

    /** Текущее состояние спутника (активен/неактивен). */
    protected SatelliteState state;

    /** Система управления энергией спутника. */
    protected EnergySystem energy;

    /**
     * Минимальный уровень заряда батареи (в долях от 1), при котором возможна активация спутника.
     * Значение: {@value}.
     */
    private static final double MIN_POSSIBLE_BATTERY_FOR_ACTIVATE = 0.15;

    /**
     * Конструктор спутника.
     *
     * @param aName   базовое имя спутника (например, "SAT")
     * @param aNumber уникальный номер, добавляемый к имени (например, 123)
     */
    protected Satellite(String aName, int aNumber) {
        name = generateName(aName, aNumber);
        state = new SatelliteState();
        energy = new EnergySystem();
        System.out.printf("\uD83D\uDEF0\uFE0F Создан спутник: %s (заряд: %.0f%%)%n", name, energy.getBatteryLevel() * 100.0);
    }

    /**
     * Деактивирует спутник, переводя его состояние в неактивное.
     */
    public void deactivate() {
        state.deactivate();
    }

    /**
     * Возвращает имя спутника.
     *
     * @return имя спутника в формате {@code "<aName>-<aNumber>"}
     */
    public String getName() {
        return name;
    }

    /**
     * Генерирует уникальное имя спутника, объединяя базовое имя и номер.
     *
     * @param name   базовое имя
     * @param number номер спутника
     * @return сформированное имя вида {@code "name-number"}
     */
    private String generateName(String name, int number) {
        return name + "-" + number;
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
     * @return {@code true}, если активация прошла успешно (включая случай, когда спутник уже был активен),
     *         {@code false} — если активация невозможна из-за низкого заряда батареи
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
            System.out.printf("⛔ %s: Ошибка активации (заряд: %.0f%%)%n", name, energy.getBatteryLevel() * 100);
            return false;
        }
    }

    /**
     * Обрабатывает изменение уровня заряда батареи.
     * <p>
     * Если уровень заряда падает до или ниже порога {@link #MIN_POSSIBLE_BATTERY_FOR_ACTIVATE}
     * и спутник в данный момент активен, он автоматически деактивируется.
     * </p>
     */
    protected void handleChangeBatteryLevel() {
        if (energy.getBatteryLevel() <= MIN_POSSIBLE_BATTERY_FOR_ACTIVATE && state.isActive()) state.deactivate();
    }

    /**
     * Выполняет основную миссию спутника.
     * <p>
     * Этот метод должен быть реализован в подклассах и определяет специфическое поведение
     * конкретного типа спутника (например, передача данных, наблюдение за Землёй и т.д.).
     * </p>
     */
    protected abstract void performMission();
}