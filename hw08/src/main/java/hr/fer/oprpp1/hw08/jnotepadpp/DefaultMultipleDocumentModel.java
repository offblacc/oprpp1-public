package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel, SingleDocumentListener, WindowListener {
    /**
     * A collection of the documents this model contains.
     */
    private List<SingleDocumentModel> documents = new ArrayList<>();

    /**
     * A reference to the currently selected document.
     */
    private SingleDocumentModel currentDocument;

    /**
     * A collection of listeners that are notified when the currently selected document is changed, a new document is
     * opened or a document is closed.
     */
    private List<MultipleDocumentListener> listeners = new ArrayList<>();

    /**
     * Keeps track of the currently opened tab.
     */
    private int currentTabIndex = 0;

    /**
     * Constructor that initializes the model.
     */
    public DefaultMultipleDocumentModel() {
        super();
        // fired whenever a tab is selected -> need to update the current document
        this.addChangeListener(e -> {
            int index = this.getSelectedIndex();
            if (index == -1) {
                this.currentDocument = null;
                return;
            }
            var prevDocument = currentDocument;
            currentDocument = this.documents.get(index);
            currentTabIndex = index;
            notifyListenersCurrentDocumentChanged(prevDocument, currentDocument);
        });
    }


    /**
     * {@inheritDoc} This particular implementation returns this, as this class extends {@link JTabbedPane} and is
     * therefore a {@link JComponent}.
     */
    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SingleDocumentModel createNewDocument() {
        DefaultSingleDocumentModel newDocument = new DefaultSingleDocumentModel(null, null);
        documents.add(newDocument);
        addTab("(unnamed)", newDocument.getIcon(), new JScrollPane(newDocument.getTextComponent()));
        setSelectedIndex(documents.size() - 1);
        currentDocument = newDocument;
        currentTabIndex = documents.size() - 1;
        setToolTipTextAt(currentTabIndex, "(unnamed)");
        newDocument.addSingleDocumentListener(this);
        notifyListenersDocumentAdded(newDocument);
        revalidate();
        repaint();
        return newDocument;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SingleDocumentModel getCurrentDocument() {
        return currentDocument;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SingleDocumentModel loadDocument(Path path) {
        if (path == null) throw new NullPointerException("Path must not be null.");

        SingleDocumentModel newModel = getOpenDocumentAtPath(path);
        if (newModel == null) {
            try {
                String text = new String(Files.readAllBytes(path));
                newModel = new DefaultSingleDocumentModel(text, path);
            } catch (IOException e) {
                throw new RuntimeException("Error while reading from file.");
            }

            documents.add(newModel);
            this.addTab(path.getFileName().toString(), ((DefaultSingleDocumentModel) newModel).getIcon(), new JScrollPane(newModel.getTextComponent()));
            setSelectedIndex(documents.size() - 1);
            currentTabIndex = documents.size() - 1;
            setToolTipTextAt(currentTabIndex, path.toString());
            for (var l : listeners) l.documentAdded(newModel);
            revalidate();
            repaint();
        } else {
            setSelectedIndex(documents.indexOf(newModel));
            currentTabIndex = documents.indexOf(newModel);
        }
        newModel.addSingleDocumentListener(this);
        currentDocument = newModel;
        return newModel;
    }

    /**
     * Checks whether the document on the given path is already opened, returns a reference to it if it is, null
     * otherwise.
     *
     * @param path the path to the document to be checked
     * @return a reference to the document on the given path if it is already opened, null otherwise
     */
    private SingleDocumentModel getOpenDocumentAtPath(Path path) {
        for (SingleDocumentModel document : documents) {
            if (document.getFilePath() != null && document.getFilePath().equals(path)) {
                return document;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        if (model == null) throw new NullPointerException("Model must not be null.");
        if (newPath == null) {
            newPath = model.getFilePath();
            if (newPath == null) throw new NullPointerException("Cannot save document without a path.");
        }

        try {
            Files.write(newPath, model.getTextComponent().getText().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to file.");
        }

        model.setModified(false);
        model.setFilePath(newPath);
        int index = documents.indexOf(model);
        setTitleAt(index, newPath.getFileName().toString());
        revalidate();
        repaint();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeDocument(SingleDocumentModel model) {
        documents.remove(model);
        removeTabAt(currentTabIndex);
        listeners.forEach(l -> l.documentRemoved(model));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.add(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfDocuments() {
        return getTabCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SingleDocumentModel getDocument(int index) {
        return documents.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SingleDocumentModel findForPath(Path path) {
        for (SingleDocumentModel document : documents) {
            if (document.getFilePath() != null && document.getFilePath().equals(path)) {
                return document;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc} Returns -1 if the given document is not in the collection of documents.
     *
     * @param doc the document model whose index is to be returned
     * @return the index of the given document model, or -1 if the given document is not in the collection of documents
     */
    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        return documents.indexOf(doc);
    }

    /**
     * Returns an iterator over the documents in this model, delegating to the iterator of the collection of documents.
     *
     * @return an iterator over the documents in this model
     */
    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documents.iterator();
    }

    /**
     * Notifies all the listeners that the currently selected document has changed.
     *
     * @param previousModel the previously selected document, or null if there was no previously selected document
     * @param currentModel  the currently selected document, or null if there is no currently selected document
     */
    private void notifyListenersCurrentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        listeners.forEach(l -> l.currentDocumentChanged(previousModel, currentModel));
    }

    /**
     * Notifies all the listeners that a new document has been opened.
     *
     * @param model the newly opened document
     */
    private void notifyListenersDocumentAdded(SingleDocumentModel model) {
        listeners.forEach(l -> l.documentAdded(model));
    }

    /**
     * Notifies all the listeners that a document has been closed.
     *
     * @param model the closed document
     */
    private void notifyListenersDocumentRemoved(SingleDocumentModel model) {
        listeners.forEach(l -> l.documentRemoved(model));
    }


    /**
     * {@inheritDoc}
     * Sets the right icon for the tab.
     */
    @Override
    public void documentModifyStatusUpdated(SingleDocumentModel model) {
        int index = documents.indexOf(model);
        setIconAt(index, ((DefaultSingleDocumentModel) model).getIcon());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void documentFilePathUpdated(SingleDocumentModel model) {

    }

    /**
     * {@inheritDoc}
     * Checks for unsaved changes and asks the user if they want to save them. Exits the program if the user confirms.
     */
    @Override
    public void windowClosing(WindowEvent e) {
        for (SingleDocumentModel document : documents) {
            if (document.isModified()) {
                String name = document.getFilePath() == null ? "unnamed" : document.getFilePath().getFileName().toString();
                int result = JOptionPane.showConfirmDialog(this, "Do you want to save changes to " + name + "?", "Save changes", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    Path path = document.getFilePath();
                    if (document.getFilePath() == null) {
                        JFileChooser fc = new JFileChooser();
                        fc.setDialogTitle("Save document");
                        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                            return;
                        }
                        path = fc.getSelectedFile().toPath();
                    }
                    saveDocument(document, path);
                } else if (result == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
        }
        System.exit(0);
    }



    // unused methods from interfaces
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
