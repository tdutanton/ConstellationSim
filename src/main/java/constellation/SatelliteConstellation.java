package constellation;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для управления спутниковой группировкой.
 * <p>
 * Представляет собой коллекцию спутников ({@link Satellite}), позволяя
 * добавлять спутники, активировать их и запускать выполнение миссий
 * для всех членов группировки одновременно.
 * </p>
 */
@Getter
public class SatelliteConstellation {
    /**
     * Название спутниковой группировки.
     */
    private final String constellationName;

    /**
     * Список спутников, входящих в состав группировки.
     */
    private final List<Satellite> satellites;

    /**
     * Строка-разделитель, используемая при выводе в консоль.
     */
    private static final String NEW_LINE_DELIM_CONSTELLATION = "---------------------";

    /**
     * Название группировки по умолчанию, используемое при вызове конструктора без параметров.
     */
    private static final String DEFAULT_CONSTELLATION_NAME = "Default Constellation";

    /**
     * Флаг, включающий или отключающий подробный вывод в консоль
     * при создании, модификации и управлении группировкой.
     */
    @Setter
    private static boolean consolePrintMode = false;

    /**
     * Конструирует спутниковую группировку с заданным названием.
     *
     * @param name название группировки
     */
    public SatelliteConstellation(String name) {
        constellationName = name;
        this.satellites = new ArrayList<>();
        printConstellationCtor();
    }

    /**
     * Конструирует спутниковую группировку с названием по умолчанию ({@link #DEFAULT_CONSTELLATION_NAME}).
     */
    public SatelliteConstellation() {
        constellationName = DEFAULT_CONSTELLATION_NAME;
        this.satellites = new ArrayList<>();
        printConstellationCtor();
    }

    /**
     * Выводит сообщение о создании группировки, если включён режим {@link #consolePrintMode}.
     */
    private void printConstellationCtor() {
        if (consolePrintMode) System.out.printf("Создана спутниковая группировка: %s%n", constellationName);
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
        if (consolePrintMode)
            System.out.printf("%s добавлен в группировку '%s'%n", satellite.getName(), constellationName);
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
        if (consolePrintMode)
            System.out.printf("%s удален из группировки '%s'%n", satellite.getName(), constellationName);
    }

    /**
     * Запускает выполнение миссий для всех спутников в группировке.
     * <p>
     * Вызывает метод {@link Satellite#performMission()} у каждого спутника.
     * При включённом {@code consolePrintMode} выводится заголовок операции.
     * </p>
     */
    public void executeAllMissions() {
        if (consolePrintMode) {
            System.out.printf("Выполнение миссий группировки %s%n", constellationName);
            System.out.println(NEW_LINE_DELIM_CONSTELLATION);
        }
        for (Satellite s : satellites) {
            s.performMission();
        }
    }

    /**
     * Активирует все спутники в группировке.
     * <p>
     * Вызывает метод {@link Satellite#activate()} для каждого спутника.
     * Если группировка пуста, выводится соответствующее сообщение (при включённом {@code consolePrintMode}).
     * </p>
     */
    public void activateSatellites() {
        if (consolePrintMode) {
            System.out.println("АКТИВАЦИЯ СПУТНИКОВ");
            System.out.println(NEW_LINE_DELIM_CONSTELLATION);
        }
        if (!satellites.isEmpty()) satellites.forEach(Satellite::activate);
        else {
            if (consolePrintMode)
                System.out.printf("⛔ Активация спутников невозможна. В группировке %s отсутствуют спутники", constellationName);
        }
        if (consolePrintMode) System.out.println(NEW_LINE_DELIM_CONSTELLATION);
    }

    /**
     * Возвращает список текущих состояний всех спутников в группировке.
     * <p>
     * Для каждого спутника вызывается метод {@link Satellite#getState()},
     * и результаты собираются в новый список типа {@link SatelliteState}.
     * Порядок состояний в списке соответствует порядку спутников в группировке.
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
     * Формат строки: <code>{название=SatelliteConstellation{constellationName=..., satellites=...}}</code>,
     * где содержимое поля {@code satellites} выводится через стандартный метод {@code toString()} списка.
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
}