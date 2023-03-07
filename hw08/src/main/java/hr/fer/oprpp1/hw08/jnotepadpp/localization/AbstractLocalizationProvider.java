package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract localization provider.
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
    /**
     * List of listeners.
     */
    List<ILocalizationListener> listeners = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLocalizationListener(ILocalizationListener l) {
        listeners.add(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLocalizationListener(ILocalizationListener l) {
        listeners.remove(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String getString(String key);

    /**
     * Returns the current language.
     */
    @Override
    public abstract String getCurrentLanguage();

    /**
     * Notifies all listeners that the localization has changed.
     */
    public void fire() {
        listeners.forEach(l -> l.localizationChanged());
    }
}
