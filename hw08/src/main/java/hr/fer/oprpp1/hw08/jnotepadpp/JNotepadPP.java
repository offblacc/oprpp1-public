package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.*;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.statusbar.StatusBar;

import javax.swing.*;
import java.text.Collator;
import java.util.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.nio.file.Path;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * The main class of the application.
 */
public class JNotepadPP extends JFrame implements MultipleDocumentListener {

    /**
     * The model that holds all the documents.
     */
    private DefaultMultipleDocumentModel multipleDocumentModel;

    /**
     * The status bar.
     */
    private StatusBar statusBar;

    /**
     * The localization provider.
     */
    FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

    /**
     * List of actions that set the language.
     */
    private List<Action> languageActions;

    /**
     * Entry point of the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JNotepadPP());
    }

    /**
     * Basic constructor.
     */
    public JNotepadPP() {
        super();
        initGUI();
        setVisible(true);
    }

    /**
     * Initializes the GUI.
     */
    private void initGUI() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocation(400, 200);
        setTitle("JNotepad++");
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        multipleDocumentModel = new DefaultMultipleDocumentModel();
        statusBar = new StatusBar(multipleDocumentModel);
        addWindowListener(multipleDocumentModel);
        addWindowListener(statusBar); // statusbar is the mediator between its clock and the window
        multipleDocumentModel.addMultipleDocumentListener(this); // so that the parent can change the window title on document change
        multipleDocumentModel.addMultipleDocumentListener(statusBar); // so that the status bar can update its labels
        cp.add(multipleDocumentModel, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.SOUTH);

        createActions();
        createMenus();
        createToolbars();
    }

    /**
     * Creates the toolbars.
     */
    private void createToolbars() {
        JToolBar toolBar = new JToolBar("Alati");
        toolBar.setFloatable(true);

        toolBar.add(new JButton(newDocumentAction));
        toolBar.add(new JButton(openDocumentAction));
        toolBar.add(new JButton(saveDocumentAction));
        toolBar.add(new JButton(saveAsDocumentAction));
        toolBar.add(new JButton(deleteSelectedPartAction));
        toolBar.add(new JButton(toggleCaseAction));

        this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    /**
     * Creates the menus.
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new LocalizedJMenu("file", flp);
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(newDocumentAction));
        fileMenu.add(new JMenuItem(openDocumentAction));
        fileMenu.add(new JMenuItem(saveDocumentAction));
        fileMenu.add(new JMenuItem(saveAsDocumentAction));
        fileMenu.add(new JMenuItem(closeDocumentAction));
        fileMenu.addSeparator();

        JMenu editMenu = new LocalizedJMenu("edit", flp);

        editMenu.add(new JMenuItem(deleteSelectedPartAction));
        editMenu.add(new JMenuItem(toggleCaseAction));
        editMenu.addSeparator();
        menuBar.add(editMenu);

        JMenu toolsMenu = new LocalizedJMenu("tools", flp);
        JMenu changeCaseSubMenu = new LocalizedJMenu("changecase", flp);
        changeCaseSubMenu.add(new JMenuItem(toUpperCaseAction));
        changeCaseSubMenu.add(new JMenuItem(toLowerCaseAction));
        changeCaseSubMenu.add(new JMenuItem(toggleCaseAction));
        toolsMenu.add(changeCaseSubMenu);
        toolsMenu.addSeparator();
        menuBar.add(toolsMenu);

        JMenu sortMenu = new LocalizedJMenu("sort", flp);
        sortMenu.add(new JMenuItem(ascendingSortAction));
        sortMenu.add(new JMenuItem(descendingSortAction));
        toolsMenu.add(sortMenu);

        toolsMenu.add(new JMenuItem(uniqueAction));


        JMenu languageMenu = new LocalizedJMenu("language", flp);
        for (Action action : languageActions) {
            languageMenu.add(new JMenuItem(action));
        }

        languageMenu.addSeparator();
        menuBar.add(languageMenu);

        this.setJMenuBar(menuBar);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        updateTitle(currentModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void documentAdded(SingleDocumentModel model) {
        updateTitle(model);
        ((DefaultSingleDocumentModel) model).addCaretListener(statusBar); // any time a document is added, add the status bar as it's
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void documentRemoved(SingleDocumentModel model) {
        updateTitle(multipleDocumentModel.getCurrentDocument());
    }

    /**
     * Updates the title of the window.
     *
     * @param model the model to get the title from
     */
    private void updateTitle(SingleDocumentModel model) {
        if (model == null) {
            setTitle("JNotepad++");
        } else {
            Path path = model.getFilePath();
            if (path == null) setTitle("(untitled) - JNotepad++");
            else setTitle(path.getFileName().toString() + " - JNotepad++");
        }
    }

    // ====================== CREATING ACTIONS ======================
    private final Action openDocumentAction = new LocalizableAction("open", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            var fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Open file");
            if (fileChooser.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) return;
            Path path = fileChooser.getSelectedFile().toPath();
            multipleDocumentModel.loadDocument(path);
        }
    };

    private final Action saveAsDocumentAction = new LocalizableAction("saveAs", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            var currentDocument = multipleDocumentModel.getCurrentDocument();
            if (currentDocument == null) return;
            var fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save file");
            if (fileChooser.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) return;
            Path path = fileChooser.getSelectedFile().toPath();
            multipleDocumentModel.saveDocument(currentDocument, path);
        }
    };

    private final Action saveDocumentAction = new LocalizableAction("save", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            var currentDocument = multipleDocumentModel.getCurrentDocument();
            if (currentDocument == null) return;
            if (currentDocument.getFilePath() == null) {
                saveAsDocumentAction.actionPerformed(e);
            } else {
                multipleDocumentModel.saveDocument(currentDocument, currentDocument.getFilePath());
            }
        }
    };

    private final Action closeDocumentAction = new LocalizableAction("close", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            var currentDocument = multipleDocumentModel.getCurrentDocument();
            if (currentDocument == null) return;
            if (currentDocument.isModified()) {
                var result = JOptionPane.showConfirmDialog(JNotepadPP.this, "Do you want to save the file?");
                if (result == JOptionPane.YES_OPTION) {
                    saveDocumentAction.actionPerformed(e);
                } else if (result == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            multipleDocumentModel.closeDocument(currentDocument);
        }
    };

    private final Action deleteSelectedPartAction = new LocalizableAction("delete", flp) {

        private static final long serialVersionUID = 1L;


        @Override
        public void actionPerformed(ActionEvent e) {
            var currDoc = multipleDocumentModel.getCurrentDocument();
            if (currDoc == null) return;
            JTextArea editor = currDoc.getTextComponent();
            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
            if (len == 0) return;
            int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            try {
                doc.remove(offset, len);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    };

    private final Action newDocumentAction = new LocalizableAction("new", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            multipleDocumentModel.createNewDocument();
        }
    };

    private final Action toggleCaseAction = new LocalizableAction("toggle", flp) {

        private static final long serialVersionUID = 1L;


        @Override
        public void actionPerformed(ActionEvent e) {
            var currDoc = multipleDocumentModel.getCurrentDocument();
            if (currDoc == null) return;
            JTextArea editor = currDoc.getTextComponent();

            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
            int offset = 0;
            if (len != 0) {
                offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            } else {
                len = doc.getLength();
            }
            try {
                String text = doc.getText(offset, len);
                text = changeCase(text);
                doc.remove(offset, len);
                doc.insertString(offset, text, null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        private String changeCase(String text) {
            char[] znakovi = text.toCharArray();
            for (int i = 0; i < znakovi.length; i++) {
                char c = znakovi[i];
                if (Character.isLowerCase(c)) {
                    znakovi[i] = Character.toUpperCase(c);
                } else if (Character.isUpperCase(c)) {
                    znakovi[i] = Character.toLowerCase(c);
                }
            }
            return new String(znakovi);
        }
    };

    private final Action toUpperCaseAction = new LocalizableAction("upper", flp) {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            var currDoc = multipleDocumentModel.getCurrentDocument();
            if (currDoc == null) return;
            JTextArea editor = currDoc.getTextComponent();

            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
            int offset = 0;
            if (len != 0) {
                offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            } else {
                len = doc.getLength();
            }
            try {
                String text = doc.getText(offset, len);
                text = text.toUpperCase();
                doc.remove(offset, len);
                doc.insertString(offset, text, null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

    };

    private final Action toLowerCaseAction = new LocalizableAction("lower", flp) {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            var currDoc = multipleDocumentModel.getCurrentDocument();
            if (currDoc == null) return;
            JTextArea editor = currDoc.getTextComponent();

            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
            int offset = 0;
            if (len != 0) {
                offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            } else {
                len = doc.getLength();
            }
            try {
                String text = doc.getText(offset, len);
                text = text.toLowerCase();
                doc.remove(offset, len);
                doc.insertString(offset, text, null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

    };

    private void sortText(boolean ascending) {
        var currDoc = multipleDocumentModel.getCurrentDocument();
        if (currDoc == null) return;
        JTextArea editor = currDoc.getTextComponent();

        Document doc = editor.getDocument();
        int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
        int offset = 0;
        if (len != 0) {
            offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
        } else {
            len = doc.getLength();
        }
        try {
            String text = doc.getText(offset, len);
            text = sort(text, ascending);
            doc.remove(offset, len);
            doc.insertString(offset, text, null);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private String sort(String text, boolean ascending) {
        String[] lines = text.split("\\r?\\n");
        Arrays.sort(lines, Collator.getInstance(new Locale("hr", "HR")));
        if (!ascending) {
            Collections.reverse(Arrays.asList(lines));
        }
        return String.join(System.lineSeparator(), lines);
    }

    private final Action ascendingSortAction = new LocalizableAction("ascending", flp) {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            sortText(true);
        }
    };

    private final Action descendingSortAction = new LocalizableAction("descending", flp) {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            sortText(false);
        }
    };

    private final Action uniqueAction = new LocalizableAction("unique", flp) {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            var currDoc = multipleDocumentModel.getCurrentDocument();
            if (currDoc == null) return;
            JTextArea editor = currDoc.getTextComponent();

            Document doc = editor.getDocument();
            int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
            int offset = 0;
            if (len != 0) {
                offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
            } else {
                len = doc.getLength();
            }
            try {
                String text = doc.getText(offset, len);
                text = unique(text);
                doc.remove(offset, len);
                doc.insertString(offset, text, null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        private String unique(String text) {
            String[] lines = text.split("\\r?\\n");
            Set<String> set = new LinkedHashSet<>(Arrays.asList(lines));
            return String.join(System.lineSeparator(), set);
        }
    };


    private void createActions() {
        openDocumentAction.putValue(Action.NAME, flp.getString("open"));
        openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
        openDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to open existing file from disk.");

        saveDocumentAction.putValue(Action.NAME, flp.getString("save"));
        saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        saveDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to save current file to disk.");

        saveAsDocumentAction.putValue(Action.NAME, flp.getString("saveAs"));
        saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
        saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        saveAsDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to save current file to disk with new name.");

        newDocumentAction.putValue(Action.NAME, flp.getString("new"));
        newDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
        newDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to create new file.");

        closeDocumentAction.putValue(Action.NAME, flp.getString("close"));
        closeDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
        closeDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
        closeDocumentAction.putValue(Action.SHORT_DESCRIPTION, "Used to close current file.");

        deleteSelectedPartAction.putValue(Action.NAME, flp.getString("delete"));
        deleteSelectedPartAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F2"));
        deleteSelectedPartAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
        deleteSelectedPartAction.putValue(Action.SHORT_DESCRIPTION, "Used to delete the selected part of text.");

        toggleCaseAction.putValue(Action.NAME, flp.getString("toggle"));
        toggleCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F3"));
        toggleCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
        toggleCaseAction.putValue(Action.SHORT_DESCRIPTION, "Used to toggle character case in selected part of text or in entire document.");

        Map<String, String> languages = Languages.getLanguages();
        languageActions = new ArrayList<>();

        for (var lang : languages.entrySet()) {
            var tag = lang.getKey();
            var value = lang.getValue();
            var newAction = new AbstractAction(value) { // change to LocalizableAction if you want to use localization
                @Override
                public void actionPerformed(ActionEvent e) {
                    LocalizationProvider.getInstance().setLanguage(tag);
                }
            };
            newAction.putValue(Action.NAME, value);
            newAction.putValue(Action.SHORT_DESCRIPTION, "Used to change language to " + value + ".");
            languageActions.add(newAction);
        }
    }
}