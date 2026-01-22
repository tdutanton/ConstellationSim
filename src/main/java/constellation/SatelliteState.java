package constellation;

/**
 * Класс, представляющий состояние спутника - активен или неактивен.
 * <p>
 * Используется для отслеживания текущего режима работы спутника. По умолчанию
 * спутник создаётся в неактивном состоянии.
 * </p>
 */
public class SatelliteState {
    /** Флаг, указывающий, активен ли спутник. */
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
     * Проверяет, активен ли спутник.
     *
     * @return {@code true}, если спутник активен; {@code false} - если неактивен
     */
    public boolean isActive() {
        return isActive;
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
}