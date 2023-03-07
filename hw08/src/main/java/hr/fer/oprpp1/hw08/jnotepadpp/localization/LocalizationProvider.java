package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class that represents localization provider.
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
    /**
     * Current language
     */
    private String language;
    /**
     * Resource bundle containg translations
     */
    private ResourceBundle bundle;
    /**
     * Singleton instane of this class
     */
    private static final LocalizationProvider instance = new LocalizationProvider(); // singleton

    /**
     * Constructor that sets language to default and loads translations.
     */
    private LocalizationProvider() {
        super();
        language = "hr";
        bundle = ResourceBundle.getBundle("localization/translations", Locale.forLanguageTag(language));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentLanguage() {
        return language == null ? "en" : language;
    }

    /**
     * Sets language to given language.
     * @param language language to set
     */
    public void setLanguage(String language) {
        this.language = language;
        bundle = ResourceBundle.getBundle("localization/translations", Locale.forLanguageTag(language));
        fire();
    }

    /**
     * Returns the singleton instance of this class.
     * @return the singleton instance of this class
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }
}
