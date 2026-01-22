package constellation;

/**
 * Абстрактный базовый класс для представления спутника.
 * Содержит общую логику управления состоянием спутника: активация/деактивация,
 * контроль уровня заряда батареи и выполнение миссий.
 * Потомки должны реализовать метод {@link #performMission()}.
 */
public abstract class Satellite {
    /**
     * Имя спутника, генерируется автоматически при создании.
     */
    protected String name;

    /**
     * Текущее состояние спутника (активен/неактивен).
     */
    protected SatelliteState state;

    /**
     * Система управления энергией спутника.
     */
    protected EnergySystem energy;

    /**
     * Минимальный допустимый уровень заряда для активации спутника (20%).
     */
    private static final double MIN_POSSIBLE_BATTERY_FOR_ACTIVATE = 0.20;

    /**
     * Флаг, включающий или отключающий подробный вывод в консоль
     * при создании, активации и выполнении операций со спутником.
     */
    protected static boolean consolePrintMode = false;

    /**
     * Устанавливает режим вывода подробной информации в консоль.
     *
     * @param state {@code true} — включить вывод, {@code false} — отключить.
     */
    public static void SetConsolePrintMode(boolean state) {
        consolePrintMode = state;
    }

    /**
     * Конструктор спутника.
     * Генерирует уникальное имя на основе переданного префикса и номера,
     * инициализирует начальный уровень заряда батареи случайным значением,
     * устанавливает состояние как неактивное.
     * При включённом {@code consolePrintMode} выводит сообщение о создании спутника.
     *
     * @param aName   префикс имени спутника (например, "Связь" или "ДЗЗ")
     * @param aNumber последовательный номер спутника
     */
    protected Satellite(String aName, int aNumber) {
        name = generateName(aName, aNumber);
        state = new SatelliteState();
        energy = new EnergySystem();
        System.out.printf("\uD83D\uDEF0\uFE0F Создан спутник: %s (заряд: %.0f%%)%n", name, energy.getBatteryLevel() * 100.0);
    }

    /**
     * Возвращает имя спутника.
     *
     * @return имя спутника
     */
    public String getName() {
        return name;
    }

    /**
     * Генерирует уникальное имя спутника на основе префикса и номера.
     *
     * @param name   префикс имени
     * @param number последовательный номер
     * @return сформированное имя вида "префикс-номер"
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
     * {@code false} — если активация невозможна из-за низкого заряда батареи
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
     * Проверяет уровень заряда и деактивирует спутник, если он опустился
     * до или ниже порогового значения {@link #MIN_POSSIBLE_BATTERY_FOR_ACTIVATE}.
     * Вызывается автоматически после операций, расходующих заряд.
     */
    protected void handleChangeBatteryLevel() {
        if (energy.getBatteryLevel() <= MIN_POSSIBLE_BATTERY_FOR_ACTIVATE && state.isActive()) state.deactivate();
    }

    /**
     * Выполняет основную миссию спутника.
     * Конкретная реализация зависит от типа спутника и должна быть предоставлена
     * в наследующих классах (например, передача данных или съёмка).
     */
    protected abstract void performMission();

    public String status() {
        String result = "не активен";
        if (state.isActive()) result = "активен";
        return "Спутник " + getName() + " . Статус:  " + result;
    }
}