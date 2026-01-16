/**
 * Класс, представляющий спутник зондирования — подкласс {@link Satellite}.
 * <p>
 * Спутник предназначен для выполнения съёмки местности с заданным пространственным разрешением.
 * При каждой миссии он делает один снимок, увеличивает счётчик сделанных фотографий и потребляет
 * заряд батареи. Если уровень заряда падает ниже допустимого порога, спутник автоматически
 * деактивируется.
 * </p>
 */
public class ImagingSatellite extends Satellite {
    /** Пространственное разрешение съёмки в метрах на пиксель. */
    private final double resolution;

    /** Количество снимков, сделанных спутником с момента создания. */
    private int photosTaken;

    /** Имя по умолчанию для спутников дистанционного зондирования. */
    private static final String DEFAULT_NAME = "ДЗЗ";

    /** Последовательный серийный номер, присваиваемый каждому новому спутнику ДЗЗ. */
    private static int SERIAL_NUMBER = 1;

    /**
     * Уровень потребления заряда батареи за одно выполнение миссии (съёмку).
     * Значение: {@value}.
     */
    private static final double BATTERY_PER_MISSION = 0.05;

    /**
     * Конструктор спутника дистанционного зондирования.
     *
     * @param aResolution пространственное разрешение съёмки в метрах на пиксель; должно быть неотрицательным
     * @throws IllegalArgumentException если {@code aResolution} меньше нуля
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
     * Возвращает строковое представление спутника ДЗЗ.
     * @return строковое описание объекта
     */
    @Override
    public String toString() {
        return String.format(
                "%s{resolution=%.1f, photosTaken=%d, name='%s' isActive=%s, batteryLevel=%.2f}",
                this.getClass().getSimpleName(),
                resolution,
                photosTaken,
                name,
                state.isActive(),
                energy.getBatteryLevel()
        );
    }

    /**
     * Возвращает разрешение съёмки спутника.
     *
     * @return разрешение в метрах на пиксель
     */
    public double getResolution() {
        return resolution;
    }

    /**
     * Возвращает количество снимков, сделанных спутником с момента его создания.
     *
     * @return число сделанных фотографий
     */
    public int getPhotosTaken() {
        return photosTaken;
    }

    /**
     * Выполняет миссию спутника ДЗЗ — съёмку территории.
     * <p>
     * Если спутник активен, он:
     * <ul>
     *   <li>выводит сообщение о начале съёмки с указанием разрешения,</li>
     *   <li>потребляет заряд батареи согласно {@link #BATTERY_PER_MISSION},</li>
     *   <li>проверяет, не требуется ли деактивация из-за низкого заряда,</li>
     *   <li>делает снимок, увеличивая внутренний счётчик {@link #photosTaken}.</li>
     * </ul>
     * Если спутник неактивен, выводится соответствующее сообщение, и миссия не выполняется.
     * </p>
     */
    @Override
    public void performMission() {
        if (state.isActive()) {
            System.out.printf("✅ %s: Съемка территории с разрешением %.1f м/пиксель%n", name, resolution);
            energy.consume(BATTERY_PER_MISSION);
            handleChangeBatteryLevel();
            takePhoto();
        } else {
            System.out.printf("⛔ %s: Не может выполнить съемку - не активен%n", name);
        }
    }

    /**
     * Имитирует процесс съёмки: увеличивает счётчик сделанных фотографий и выводит сообщение.
     */
    private void takePhoto() {
        photosTaken++;
        System.out.printf("✅ %s : Снимок #%d сделан!%n", name, photosTaken);
    }
}