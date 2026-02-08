package constellation;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Класс, представляющий состояние спутника - активен или неактивен.
 * <p>
 * Используется для отслеживания текущего режима работы спутника. По умолчанию
 * спутник создаётся в неактивном состоянии.
 * </p>
 */
@Getter
@EqualsAndHashCode
public class SatelliteState {
    /**
     * Флаг, указывающий, активен ли спутник.
     */
    private boolean isActive;

    /**
     * Конструктор состояния спутника.
     * <p>
     * Инициализирует спутник в неактивном состоянии ({@code false}).
     * </p>
     */
    public SatelliteState() {
        isActive = false;
    }

    /**
     * Деактивирует спутник, устанавливая его состояние в неактивное.
     */
    public void deactivate() {
        isActive = false;
    }

    /**
     * Активирует спутник, устанавливая его состояние в активное.
     */
    public void activate() {
        isActive = true;
    }

    /**
     * Возвращает строковое представление текущего состояния спутника.
     * <p>
     * Формат строки: {@code ИмяКласса{isActive=значение, statusMessage='Активен'/'Неактивен'}}.
     * Используется для удобного отображения состояния объекта при отладке или логировании.
     * </p>
     *
     * @return строка с кратким описанием активности спутника
     */
    @Override
    public String toString() {
        return String.format(
                "%s{isActive=%s, statusMessage=%s}",
                this.getClass().getSimpleName(),
                isActive,
                isActive ? "'Активен'" : "'Неактивен'"
        );
    }
}