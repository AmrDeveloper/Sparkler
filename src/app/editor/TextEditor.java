package app.editor;

import app.utils.SwingComponent;
import javafx.embed.swing.SwingNode;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;

public class TextEditor extends SwingComponent {

    private RSyntaxTextArea mTextArea;
    private RTextScrollPane mTextScrollPane;

    private Language mCurrentLanguage = Language.TEXT;
    private static final TextFormatter mFormatter = new TextFormatter();

    public TextEditor() {
        setupNewEditor();
        setDarkTheme();
    }

    private void setupNewEditor() {
        mTextArea = new RSyntaxTextArea();
        mTextScrollPane = new RTextScrollPane(mTextArea);
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(mTextScrollPane);
    }

    public RSyntaxTextArea getTextArea() {
        return mTextArea;
    }

    public void setText(String text) {
        mTextArea.setText(text);
    }

    public void setFormattedText(String text) {
        String formattedText = mFormatter.format(text, mCurrentLanguage);
        setText(formattedText);
    }

    public String getText() {
        return mTextArea.getText();
    }

    public void clearText() {
        mTextArea.setText("");
    }

    public void setEditable(boolean isEditable) {
        mTextArea.setEditable(isEditable);
    }

    public void changeLanguage(Language language) {
        switch (language) {
            case JSON: {
                changeLanguageJSON();
                break;
            }
            case HTML: {
                changeLanguageHTML();
                break;
            }
            case XML: {
                changeLanguageXML();
                break;
            }
            case TEXT: {
                changeLanguageTEXT();
                break;
            }
        }
    }

    private void changeLanguageJSON() {
        mTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        enableCodeSetup();
        mCurrentLanguage = Language.JSON;
    }

    private void changeLanguageXML() {
        mTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        enableCodeSetup();
        mCurrentLanguage = Language.XML;
    }

    private void changeLanguageHTML() {
        mTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        enableCodeSetup();
        mCurrentLanguage = Language.HTML;
    }

    private void changeLanguageTEXT() {
        mTextArea.setSyntaxEditingStyle("");
        disableCodeSetup();
        mCurrentLanguage = Language.TEXT;
    }

    public void formatText() {
        String formattedText = mFormatter.format(getText(), mCurrentLanguage);
        setText(formattedText);
    }

    public Language getCurrentLanguage() {
        return mCurrentLanguage;
    }

    private void enableCodeSetup() {
        mTextArea.setBracketMatchingEnabled(true);
        mTextArea.setCloseMarkupTags(true);
        mTextArea.setCloseCurlyBraces(true);
        mTextArea.setCodeFoldingEnabled(true);
    }

    private void disableCodeSetup() {
        mTextArea.setBracketMatchingEnabled(false);
        mTextArea.setCloseMarkupTags(false);
        mTextArea.setCloseCurlyBraces(false);
        mTextArea.setCodeFoldingEnabled(false);
    }

    public void setWhiteTheme() {
        //TODO : Set white theme to this text editor
    }

    public void setDarkTheme() {
        //TODO : set Dark theme to this text editor

    }

    @Override
    public void invoke(SwingNode root) {
        SwingUtilities.invokeLater(() -> root.setContent(mTextScrollPane));
    }
}
