import java.util.Random;

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
     * Текущее состояние спутника: активен ({@code true}) или нет ({@code false}).
     */
    protected boolean isActive;

    /**
     * Уровень заряда батареи в диапазоне [0.0, 1.0].
     */
    protected double batteryLevel;

    /**
     * Генератор случайных чисел для инициализации начального уровня заряда.
     */
    private static final Random RANDOM = new Random();

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
        isActive = false;
        batteryLevel = generateBatteryLevel();
        if (consolePrintMode)
            System.out.printf("\uD83D\uDEF0\uFE0F Создан спутник: %s (заряд: %.0f%%)%n", name, batteryLevel * 100.0);
    }

    /**
     * Деактивирует спутник, устанавливая флаг {@link #isActive} в {@code false}.
     */
    public void deactivate() {
        isActive = false;
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
     * Генерирует случайный начальный уровень заряда батареи в диапазоне [0.0, 1.0).
     *
     * @return случайное значение уровня заряда
     */
    protected double generateBatteryLevel() {
        return RANDOM.nextDouble();
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
     * Уменьшает уровень заряда батареи на указанную величину.
     * Заряд не может опуститься ниже 0.0.
     *
     * @param amount величина, на которую снижается заряд (должна быть ≥ 0)
     * @throws IllegalArgumentException если {@code amount} отрицательное
     */
    public void consumeBattery(double amount) {
        if (amount < 0.0)
            throw new IllegalArgumentException("Значение для снижения заряда батареи не должно быть отрицательным");
        batteryLevel = Math.max(0.0, this.batteryLevel - amount);
    }

    /**
     * Пытается активировать спутник.
     * Активация возможна только если уровень заряда превышает
     * {@link #MIN_POSSIBLE_BATTERY_FOR_ACTIVATE} (15%) и спутник ещё не активен.
     * При включённом {@code consolePrintMode} выводятся соответствующие сообщения.
     *
     * @return {@code true}, если активация прошла успешно или спутник уже был активен;
     * {@code false}, если активация невозможна из-за недостаточного заряда.
     */
    public boolean activate() {
        if (batteryLevel > MIN_POSSIBLE_BATTERY_FOR_ACTIVATE && !isActive) {
            isActive = true;
            if (consolePrintMode) System.out.printf("✅ %s: Активация успешна%n", name);
            return true;
        } else if (isActive) {
            if (consolePrintMode) System.out.printf("⚠️ %s: Активация уже была произведена ранее%n", name);
            return true;
        } else {
            if (consolePrintMode)
                System.out.printf("⛔ %s: Ошибка активации (заряд: %.0f%%)%n", name, batteryLevel * 100);
            return false;
        }
    }

    /**
     * Проверяет уровень заряда и деактивирует спутник, если он опустился
     * до или ниже порогового значения {@link #MIN_POSSIBLE_BATTERY_FOR_ACTIVATE}.
     * Вызывается автоматически после операций, расходующих заряд.
     */
    protected void handleChangeBatteryLevel() {
        if (batteryLevel <= MIN_POSSIBLE_BATTERY_FOR_ACTIVATE && isActive) deactivate();
    }

    /**
     * Выполняет основную миссию спутника.
     * Конкретная реализация зависит от типа спутника и должна быть предоставлена
     * в наследующих классах (например, передача данных или съёмка).
     */
    protected abstract void performMission();
}