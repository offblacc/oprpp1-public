package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Form localization provider class which extends localization provider bridge
 * to add the ability to connect and disconnect the form localization provider
 * from the localization provider.
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {
    /**
     * Form localization provider constructor.
     *
     * @param parent - ILokalizationProvider parent, should be bridge
     * @param frame    frame to which the localization provider is connected
     */
    public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
      super(parent);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                connect();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }
        });
    }
}
