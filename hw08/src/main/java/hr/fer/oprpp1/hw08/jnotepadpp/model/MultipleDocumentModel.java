package hr.fer.oprpp1.hw08.jnotepadpp.model;

import javax.swing.*;
import java.nio.file.Path;

/**
 * Class that represents a model capable of holding zero, one or more documents of class {@link SingleDocumentModel},
 * where each document and having a concept of current document – the one which is shown to the user and on which user
 * works.
 *
 * @author offblacc
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
    /**
     * Returns the graphical component which is responsible for displaying the entire MultipleDocumentModel's user
     * interface. This component will be added into the hierarchy of graphical components which are displayed in window.
     * If the MultipleDocumentModel is graphical component itself, it will simply implement the method as: return this;
     *
     * @return the graphical component which is responsible for displaying the entire MultipleDocumentModel's user
     * interface
     */
    JComponent getVisualComponent();

    /**
     * Creates a new blank document model, adds it to the collection of models and returns a reference to it.
     *
     * @return a reference to the newly created document model
     */
    SingleDocumentModel createNewDocument();

    /**
     * Returns a reference to the currently selected document model, or the currently active, opened, if you will.
     *
     * @return a reference to the currently selected document model
     */
    SingleDocumentModel getCurrentDocument();

    /**
     * Should load a new document from the path given as argument. If the document is already opened, it should simply
     * select it and return a reference to it. If the document is not opened, it should be opened and returned as a
     * reference. The given path must not be null. Throws an IllegalArgumentException if the given path is null.
     *
     * @param path the path to the document to be loaded
     * @return a reference to the document model which was loaded
     * @throws IllegalArgumentException if the given path is null
     */
    SingleDocumentModel loadDocument(Path path);

    /**
     * Saves the given document model to the given path. If the given path is null, the method should save the document
     * to the path from which it was loaded. If the document was not loaded from any path, the method should throw an
     * IllegalArgumentException. The given document model must not be null. Throws an {@link NullPointerException} if
     * the given document model is null.
     *
     * @param model   the document model to be saved
     * @param newPath the path to which the document should be saved, or null if the document should be saved to the
     *                path from which it was loaded
     * @throws NullPointerException     if the given document model is null
     * @throws IllegalArgumentException if the given document model was not loaded from any path and the given path is
     *                                  null
     */
    void saveDocument(SingleDocumentModel model, Path newPath);

    /**
     * Closes the given document model. If the document models are saved in a list, for example, this would remove it
     * from the list of currently opened documents (and should, of course, depending on the implementation do all the
     * things that the semantics of closing a document assume). This method is not responsible for asking the user if he
     * wants to save the document before closing it. The given document model must not be null. Throws an
     * {@link NullPointerException} if the given document model is null.
     *
     * @param model the document model to be closed
     * @throws NullPointerException if the given document model is null
     */
    void closeDocument(SingleDocumentModel model);

    /**
     * Adds a new listener to the collection of listeners.
     *
     * @param l the listener to be added
     */
    void addMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Removes the given listener from the collection of listeners.
     *
     * @param l the listener to be removed
     */
    void removeMultipleDocumentListener(MultipleDocumentListener l);

    /**
     * Returns the number of currently opened documents.
     *
     * @return the number of currently opened documents
     */
    int getNumberOfDocuments();

    /**
     * Returns the document at the given index.
     *
     * @param index the index of the document to be returned
     * @return the document at the given index
     */
    SingleDocumentModel getDocument(int index);

    /**
     * Returns a reference to the {@link SingleDocumentModel} which is associated with the given {@link Path}. If no
     * such document exists, returns null.
     *
     * @param path the path to the document to be found
     * @return the document with the given path
     */
    SingleDocumentModel findForPath(Path path); //null, if no such model exists

    /**
     * Returns the index of the given document model.
     *
     * @param doc the document model whose index is to be returned
     * @return the index of the given document model
     */
    int getIndexOfDocument(SingleDocumentModel doc); //-1 if not present
}
