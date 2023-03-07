package hr.fer.oprpp1.hw08.jnotepadpp.localization;

/**
 * A bridge between {@link ILocalizationProvider} and {@link ILocalizationListener} that will notify listeners when
 * language changes.
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
    /**
     * ILocalizationProvider instance that is being bridged.
     */
    private final ILocalizationProvider parent;

    /**
     * Current connection status
     */
    private boolean connected;

    /**
     * Constructor that sets parent to given parent.
     * @param parent ILocaizationProvider instance that is being bridged
     */
    public LocalizationProviderBridge(ILocalizationProvider parent) {
        super();
        this.parent = parent;
        this.connected = true;
    }

    /**
     * Disconnects from ILocaizationProvider parent
     */
    public void disconnect() {
        if (!connected) return;
        connected = false;
        System.out.println(this.listeners.size());
        parent.removeLocalizationListener(this::fire);
    }

    /**
     * Connects to ILocaizationProvider parent
     */
    public void connect() {
        if (connected) return;
        connected = true;
        System.out.println(this.listeners.size());
        addLocalizationListener(this::fire);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String key) {
        if (connected) {
            return parent.getString(key);
        } else {
            return key;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentLanguage() {
        if (connected) {
            return parent.getCurrentLanguage();
        } else {
            return "en"; // if not connected, return english
        }
    }
}
