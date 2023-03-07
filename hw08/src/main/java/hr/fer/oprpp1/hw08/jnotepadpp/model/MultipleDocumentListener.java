package hr.fer.oprpp1.hw08.jnotepadpp.model;

/**
 * Listener that is notified when the currently selected document is changed (selecting a differenc document or closing
 * the currently selected document), a new document is opened or a document is closed.
 *
 * @author offblacc
 */
public interface MultipleDocumentListener {
    /**
     * Called when the currently selected document is changed.
     *
     * @param previousModel the previously selected document, or null if there was no previously selected document
     * @param currentModel  the currently selected document, or null if there is no currently selected document
     */
    void currentDocumentChanged(SingleDocumentModel previousModel,
                                SingleDocumentModel currentModel);

    /**
     * Called when a new document is added.
     *
     * @param model the document that was added
     */
    void documentAdded(SingleDocumentModel model);

    /**
     * Called when a document is removed, closing it.
     *
     * @param model the document that was removed
     */
    void documentRemoved(SingleDocumentModel model);

}
