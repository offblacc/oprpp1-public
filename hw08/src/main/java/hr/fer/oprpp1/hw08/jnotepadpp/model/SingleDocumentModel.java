package hr.fer.oprpp1.hw08.jnotepadpp.model;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Class that represents a model of a single document, having information about file path from which document was loaded
 * (can be null for a new document), document modification status and reference to Swing component which is used for
 * editing.
 *
 * @author offblacc
 */
public interface SingleDocumentModel {
    /**
     * Returns the reference to a <code>JTextArea</code> component which is used for editing.
     *
     * @return the reference to a <code>JTextArea</code> component which is used for editing
     */
    JTextArea getTextComponent();

    /**
     * Returns the file path from which document was loaded (can be null for a new document).
     *
     * @return the file path from which document was loaded (can be null for a new document)
     */
    Path getFilePath();

    /**
     * Sets the file path for the document. Can not be null. Throws {@link NullPointerException} if null is given.
     *
     * @param path the file path for the document (can not be null)
     * @throws NullPointerException if the given path is null
     */
    void setFilePath(Path path);

    /**
     * Returns true if the file was modified since last save.
     *
     * @return true if the file was modified since last save
     */
    boolean isModified();

    /**
     * Sets the modified flag for the document.
     *
     * @param modified the modified flag to be set, should be true if the file was modified since last save
     */
    void setModified(boolean modified);

    /**
     * Adds the given listener to the list of listeners.
     *
     * @param l the listener to be added
     */
    void addSingleDocumentListener(SingleDocumentListener l);

    /**
     * Removes the given listener from the list of listeners.
     *
     * @param l the listener to be removed
     */
    void removeSingleDocumentListener(SingleDocumentListener l);
}

