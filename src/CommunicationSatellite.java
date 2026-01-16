/**
 * Класс, представляющий спутник связи — подкласс {@link Satellite}.
 * <p>
 * Спутник связи предназначен для передачи данных с заданной пропускной способностью.
 * При выполнении миссии он потребляет заряд батареи и автоматически деактивируется,
 * если уровень заряда падает ниже допустимого порога.
 * </p>
 */
public class CommunicationSatellite extends Satellite {
    /** Пропускная способность спутника (в Мбит/с). */
    private final double bandwidth;

    /** Имя по умолчанию для спутников связи. */
    private static final String DEFAULT_NAME = "Связь";

    /** Последовательный серийный номер, присваиваемый каждому новому спутнику связи. */
    private static int SERIAL_NUMBER = 1;

    /**
     * Уровень потребления заряда батареи за одно выполнение миссии.
     * Значение: {@value}.
     */
    private static final double BATTERY_PER_MISSION = 0.09;

    /**
     * Конструктор спутника связи.
     *
     * @param aBandwidth пропускная способность спутника в Мбит/с; должно быть неотрицательным значением
     * @throws IllegalArgumentException если {@code aBandwidth} меньше нуля
     */
    public CommunicationSatellite(double aBandwidth) {
        if (aBandwidth < 0.0)
            throw new IllegalArgumentException("Пропускная способность не должна быть отрицательной");

        super(DEFAULT_NAME, SERIAL_NUMBER);
        bandwidth = aBandwidth;
        SERIAL_NUMBER++;
    }

    /**
     * Возвращает строковое представление спутника связи.
     *
     * @return строковое описание объекта
     */
    @Override
    public String toString() {
        return String.format(
                "%s{bandwidth=%s, name='%s', isActive=%s, batteryLevel=%.2f}",
                this.getClass().getSimpleName(),
                bandwidth,
                name,
                state.isActive(),
                energy.getBatteryLevel()
        );
    }

    /**
     * Возвращает пропускную способность спутника.
     *
     * @return пропускная способность в Мбит/с
     */
    public double getBandwidth() {
        return bandwidth;
    }

    /**
     * Выполняет миссию спутника связи — передачу данных.
     * <p>
     * Если спутник активен, он:
     * <ul>
     *   <li>выводит сообщение о начале передачи,</li>
     *   <li>вызывает метод {@link #sendData(double)} для имитации отправки данных,</li>
     *   <li>потребляет заряд батареи согласно {@link #BATTERY_PER_MISSION},</li>
     *   <li>проверяет, не требуется ли деактивация из-за низкого заряда.</li>
     * </ul>
     * Если спутник неактивен, выводится соответствующее сообщение, и миссия не выполняется.
     * </p>
     */
    @Override
    public void performMission() {
        if (state.isActive()) {
            System.out.printf("✅ %s: Передача данных со скоростью %.1f Мбит/с%n", name, bandwidth);
            sendData(bandwidth);
            energy.consume(BATTERY_PER_MISSION);
            handleChangeBatteryLevel();
        } else {
            System.out.printf("⛔ %s не может передать данные - не активен%n", name);
        }
    }

    /**
     * Имитирует отправку указанного объёма данных.
     *
     * @param data объём данных в Мбит (обычно равен пропускной способности спутника)
     */
    private void sendData(double data) {
        System.out.printf("✅ %s отправил %.0f Мбит данных!%n", name, data);
    }
}