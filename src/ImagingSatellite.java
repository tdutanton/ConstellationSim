/**
 * Спутник дистанционного зондирования Земли (ДЗЗ).
 * Выполняет миссию по съёмке территории с заданным пространственным разрешением.
 * Каждая миссия потребляет заряд батареи и увеличивает счётчик сделанных снимков.
 */
public class ImagingSatellite extends Satellite {
    /** Пространственное разрешение съёмки в метрах на пиксель. */
    private final double resolution;

    /** Количество сделанных снимков за время работы спутника. */
    private int photosTaken;

    /** Префикс имени по умолчанию для спутников данного типа. */
    private static final String DEFAULT_NAME = "ДЗЗ";

    /** Последовательный номер для генерации уникальных имён спутников. */
    private static int SERIAL_NUMBER = 1;

    /** Уровень потребления заряда батареи за одну миссию (8%). */
    private static final double BATTERY_PER_MISSION = 0.08;

    /**
     * Конструирует новый спутник ДЗЗ с заданным разрешением.
     * Автоматически генерирует уникальное имя на основе префикса {@link #DEFAULT_NAME}
     * и внутреннего счётчика {@link #SERIAL_NUMBER}. Инициализирует счётчик снимков значением 0.
     *
     * @param aResolution пространственное разрешение съёмки в метрах на пиксель (должно быть ≥ 0)
     * @throws IllegalArgumentException если разрешение отрицательное
     */
    public ImagingSatellite(double aResolution) {
        if (aResolution < 0.0)
            throw new IllegalArgumentException("Разрешение не должно быть отрицательным");

        super(DEFAULT_NAME, SERIAL_NUMBER);
        resolution = aResolution;
        photosTaken = 0;
        SERIAL_NUMBER++;
    }

    /**
     * Возвращает строковое представление спутника для отладки.
     * Формат: {@code ImagingSatellite{resolution=..., photosTaken=..., name='...', isActive=..., batteryLevel=...}}
     *
     * @return строка с ключевыми параметрами спутника
     */
    @Override
    public String toString() {
        return String.format(
                "%s{resolution=%.1f, photosTaken=%d, name='%s' isActive=%s, batteryLevel=%.2f}",
                this.getClass().getSimpleName(),
                resolution,
                photosTaken,
                name,
                isActive,
                batteryLevel
        );
    }

    /**
     * Возвращает пространственное разрешение съёмки.
     *
     * @return разрешение в метрах на пиксель
     */
    public double getResolution() {
        return resolution;
    }

    /**
     * Возвращает количество снимков, сделанных спутником за всё время работы.
     *
     * @return число снимков
     */
    public int getPhotosTaken() {
        return photosTaken;
    }

    /**
     * Выполняет миссию по съёмке территории.
     * Если спутник активен, он:
     * <ul>
     *   <li>выводит сообщение о начале съёмки (при включённом {@code consolePrintMode});</li>
     *   <li>расходует заряд батареи ({@link #BATTERY_PER_MISSION});</li>
     *   <li>проверяет, не требуется ли деактивация из-за низкого заряда;</li>
     *   <li>увеличивает счётчик снимков.</li>
     * </ul>
     * Если спутник не активен — выводится сообщение об ошибке.
     */
    @Override
    public void performMission() {
        if (isActive) {
            if (consolePrintMode)
                System.out.printf("✅ %s: Съемка территории с разрешением %.1f м/пиксель%n", name, resolution);
            consumeBattery(BATTERY_PER_MISSION);
            handleChangeBatteryLevel();
            takePhoto();
        } else {
            if (consolePrintMode) System.out.printf("⛔ %s: Не может выполнить съемку - не активен%n", name);
        }
    }

    /**
     * Делает один снимок.
     * Увеличивает внутренний счётчик {@link #photosTaken} на единицу.
     * При включённом {@code consolePrintMode} выводит подтверждающее сообщение.
     */
    private void takePhoto() {
        photosTaken++;
        if (consolePrintMode) System.out.printf("✅ %s : Снимок #%d сделан!%n", name, photosTaken);
    }
}