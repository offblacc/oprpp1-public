package hr.fer.oprpp1.hw08.jnotepadpp.localization;

/**
 * Interface for localization listeners.
 */
@FunctionalInterface
public interface ILocalizationListener {
    /**
     * Called when the localization changes.
     */
    void localizationChanged();
}
