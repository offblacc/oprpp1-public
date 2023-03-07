package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Class that represents an action that can be localized.
 */
public class LocalizableAction extends AbstractAction implements ILocalizationListener {
    /**
     * Reference to the localization provider.
     */
    ILocalizationProvider lp;

    /**
     * Key of the action name.
     */
    String key;

    /**
     * Constructor that creates a new localizable action.
     *
     * @param key key of the action name
     * @param lp  reference to the localization provider
     */
    public LocalizableAction(String key, ILocalizationProvider lp) {
        super(lp.getString(key));
        this.key = key;
        this.lp = lp;
        LocalizationProvider.getInstance().addLocalizationListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void localizationChanged() {
        putValue(Action.NAME, lp.getString(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
