package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that keeps track of all available languages and their tag names.
 */
public class Languages {
    /**
     * Map of all available languages and their tag names.
     */
    private static final HashMap<String, String> languages;

    static {
        languages = new HashMap<>();
        languages.put("en", "English");
        languages.put("hr", "Hrvatski");
        languages.put("de", "Deutsch");
    }

    /**
     * Returns an unmodifiable map of all available languages and their tag names.
     *
     * @return unmodifiable map of all available languages and their tag names
     */
    public static Map<String, String> getLanguages() {
        return Collections.unmodifiableMap(languages);
    }
}
