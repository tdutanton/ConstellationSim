import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий спутниковую группировку — коллекцию спутников одного или нескольких типов.
 * <p>
 * Группировка имеет имя и позволяет добавлять спутники, активировать их все сразу,
 * а также запускать выполнение миссий для всех содержащихся спутников.
 * </p>
 */
public class SatelliteConstellation {
    /** Название спутниковой группировки. */
    private final String constellationName;

    /** Список спутников, входящих в состав группировки. */
    private final List<Satellite> satellites;

    /** Разделительная строка, используемая при выводе информации о группировке. */
    private static final String NEW_LINE_DELIM_CONSTELLATION = "---------------------";

    /** Имя по умолчанию для группировки, если явное имя не задано. */
    private static final String DEFAULT_CONSTELLATION_NAME = "RU Basic";

    /**
     * Конструктор спутниковой группировки с указанием имени.
     *
     * @param name название группировки
     */
    public SatelliteConstellation(String name) {
        constellationName = name;
        this.satellites = new ArrayList<>();
        printConstellationCtor();
    }

    /**
     * Конструктор спутниковой группировки с именем по умолчанию ({@value #DEFAULT_CONSTELLATION_NAME}).
     */
    public SatelliteConstellation() {
        constellationName = DEFAULT_CONSTELLATION_NAME;
        this.satellites = new ArrayList<>();
        printConstellationCtor();
    }

    /**
     * Выводит сообщение о создании группировки в стандартный поток вывода.
     */
    private void printConstellationCtor() {
        System.out.printf("Создана спутниковая группировка: %s%n", constellationName);
    }

    /**
     * Добавляет спутник в группировку.
     * <p>
     * После добавления выводится информационное сообщение.
     * </p>
     *
     * @param satellite спутник, который необходимо добавить; не должен быть {@code null}
     */
    public void addSatellite(Satellite satellite) {
        satellites.add(satellite);
        System.out.printf("%s добавлен в группировку '%s'%n", satellite.getName(), constellationName);
    }

    /**
     * Запускает выполнение миссий для всех спутников в группировке.
     * <p>
     * Для каждого спутника вызывается метод {@link Satellite#performMission()}.
     * Перед началом выводится заголовок и разделитель.
     * </p>
     */
    public void executeAllMissions() {
        System.out.printf("Выполнение миссий группировки %s%n", constellationName);
        System.out.println(NEW_LINE_DELIM_CONSTELLATION);
        for (Satellite s : satellites) {
            s.performMission();
        }
    }

    /**
     * Возвращает неизменяемый список спутников, входящих в группировку.
     * <p>
     * Возвращается сам внутренний список, поэтому вызывающий код может модифицировать его.
     * (Если требуется защита от модификации, следует возвращать копию или обёртку.)
     * </p>
     *
     * @return список спутников группировки
     */
    public List<Satellite> getSatellites() {
        return satellites;
    }

    /**
     * Активирует все спутники в группировке.
     * <p>
     * Для каждого спутника вызывается метод {@link Satellite#activate()}.
     * Выводятся заголовок и разделители до и после активации.
     * </p>
     */
    public void activateSatellites() {
        System.out.println("АКТИВАЦИЯ СПУТНИКОВ");
        System.out.println(NEW_LINE_DELIM_CONSTELLATION);
        satellites.forEach(Satellite::activate);
        System.out.println(NEW_LINE_DELIM_CONSTELLATION);
    }
}