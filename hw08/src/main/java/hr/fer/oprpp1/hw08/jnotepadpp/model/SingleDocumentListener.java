package hr.fer.oprpp1.hw08.jnotepadpp.model;

/**
 * A listener that listens to changes in a single document.
 *
 * @author offblacc
 */
public interface SingleDocumentListener {
    /**
     * Called when the modification status of the document changes.
     *
     * @param model the document whose modification status changed
     */
    void documentModifyStatusUpdated(SingleDocumentModel model);

    /**
     * Called when the file path of the document changes.
     *
     * @param model the document whose file path changed
     */
    void documentFilePathUpdated(SingleDocumentModel model);
}
