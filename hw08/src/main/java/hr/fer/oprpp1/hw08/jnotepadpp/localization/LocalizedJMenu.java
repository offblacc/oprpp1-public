package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.*;

/**
 * Class that represents localized JMenu.
 */
public class LocalizedJMenu extends JMenu implements ILocalizationListener {
    /**
     * Localization provider used to get translations.
     */
    ILocalizationProvider lp;

    /**
     * Key used to get translation.
     */
    String key;

    /**
     * Constructor that sets localization provider and key.
     * @param key key used to get translation
     * @param lp localization provider used to get translations
     */
    public LocalizedJMenu(String key, ILocalizationProvider lp) {
        super(lp.getString(key));
        this.lp = lp;
        this.key = key;
        LocalizationProvider.getInstance().addLocalizationListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void localizationChanged() {
        setText(lp.getString(key));
    }
}
