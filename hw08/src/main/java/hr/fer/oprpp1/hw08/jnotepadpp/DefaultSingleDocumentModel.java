package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.CaretListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link SingleDocumentModel}. Omitting javadoc for methods that are explained in
 * {@link SingleDocumentModel} and don't need further explanation.
 *
 * @author offblacc
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel, KeyListener {
    /**
     * The text component that is used to display the text.
     */
    private final JTextArea textArea;

    /**
     * The path to the file represented by this model.
     */
    private Path path;

    /**
     * Keeps track of whether the file was modified since last save.
     */
    private boolean modified;

    /**
     * List of listeners that are notified when the modified flag changes.
     */
    List<SingleDocumentListener> statusListeners;

    /**
     * Icon for modified files.
     */
    private static final Icon modifiedIcon = loadIcon("/icons/modified.png");

    /**
     * Icon for unmodified files.
     */
    private static final Icon unmodifiedIcon = loadIcon("/icons/unmodified.png");

    /**
     * Loads the static final icons.
     *
     * @param path the path to the icon
     * @return the loaded icon
     */
    private static Icon loadIcon(String path) {
        try (InputStream is = DefaultSingleDocumentModel.class.getResourceAsStream(path)) {
            if (is == null) throw new NullPointerException();
            byte[] bytes = is.readAllBytes();
            if (bytes == null) throw new NullPointerException();
            return new ImageIcon(bytes);
        } catch (IOException | NullPointerException e) {
            // System.out.println("Error loading icon: " + path);
            return null; // if icon fails to load, return null, meaning it won't be displayed, silently ignoring, even though it should not happen and if it does the cause should be fixed
        }
    }

    /**
     * Creates a new instance of {@link DefaultSingleDocumentModel} with the given path and text.
     *
     * @param textContent the text to be displayed in the text component
     * @param path        the path to the file represented by this model
     */
    public DefaultSingleDocumentModel(String textContent, Path path) {
        this.textArea = new JTextArea(textContent);
        this.textArea.addKeyListener(this);
        this.path = path;
        // as it is not saved anywhere, it makes no sense to display the icon that is associated with saved files that have not been modified since last save
        this.modified = false;
        this.statusListeners = new ArrayList<>();
    }

    /**
     * Returns the icon for this tab depending on whether the file was modified since last save.
     *
     * @return the icon for this tab depending on whether the file was modified since last save
     */
    Icon getIcon() {
        return modified ? modifiedIcon : unmodifiedIcon;
    }

    /**
     * Returns the text component that is used to display the text.
     *
     * @return the text component that is used to display the text
     */
    @Override
    public JTextArea getTextComponent() {
        return textArea;
    }

    /**
     * Returns the path to the file represented by this model.
     *
     * @return the path to the file represented by this model
     */
    @Override
    public Path getFilePath() {
        return path;
    }

    /**
     * {@inheritDoc} Notifies all listeners that the file path has changed.
     */
    @Override
    public void setFilePath(Path path) {
        boolean notify = !path.equals(this.path);
        this.path = path;
        if (notify) notifyListenersPathUpdated();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isModified() {
        return modified;
    }

    /**
     * {@inheritDoc} Notifies all listeners that the modified flag has changed.
     */
    @Override
    public void setModified(boolean modified) {
        boolean notify = this.modified != modified;
        this.modified = modified;
        if (notify) notifyListenersModifiedStatusUpdated();
    }

    /**
     * Adds a caret listener
     *
     * @param listener the listener to be added
     */
    public void addCaretListener(CaretListener listener) {
        textArea.addCaretListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        statusListeners.add(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        statusListeners.remove(l);
    }

    /**
     * Notifies all listeners that the path to this document model has changed.
     */
    public void notifyListenersPathUpdated() {
        statusListeners.forEach(l -> l.documentFilePathUpdated(this));
    }

    /**
     * Notifies all listeners that the modified flag of this document model has changed.
     */
    public void notifyListenersModifiedStatusUpdated() {
        statusListeners.forEach(l -> l.documentModifyStatusUpdated(this));
    }

    /**
     * {@inheritDoc}
     * Sets the modified flag.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if (modified) return; // if it was already set to modified, no need to notify listeners on every key press
        if (e.isControlDown())
            return; // ignore control keys, they don't change the text, only mess up the modified flag
        setModified(true);
        notifyListenersModifiedStatusUpdated();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }
}
