package app.utils;

import javafx.embed.swing.SwingNode;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class TextEditor extends SwingComponent{

    private RSyntaxTextArea mTextArea;
    private RTextScrollPane mTextScrollPane;

    private Language mCurrentLanguage = Language.TEXT;
    private static final TextFormatter mFormatter = new TextFormatter();

    public TextEditor() {
        setupNewEditor();
    }

    private void setupNewEditor() {
        mTextArea = new RSyntaxTextArea();
        mTextArea.setBracketMatchingEnabled(true);
        mTextArea.setCloseMarkupTags(true);
        mTextArea.setCloseCurlyBraces(true);
        mTextArea.setCodeFoldingEnabled(true);
        mTextScrollPane = new RTextScrollPane(mTextArea);
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(mTextScrollPane);
    }

    public RSyntaxTextArea getTextArea(){
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

    public void setEditable(boolean isEditable){
        mTextArea.setEditable(isEditable);
    }

    public void changeLanguageJSON() {
        mTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        mCurrentLanguage = Language.JSON;
    }

    public void changeLanguageXML() {
        mTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        mCurrentLanguage = Language.XML;
    }

    public void changeLanguageHTML() {
        mTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        mCurrentLanguage = Language.HTML;
    }

    public void changeLanguageTEXT() {
        mTextArea.setSyntaxEditingStyle("");
        mCurrentLanguage = Language.TEXT;
    }

    public void formatText() {
        String formattedText = mFormatter.format(getText(), mCurrentLanguage);
        setText(formattedText);
    }

    public Language getCurrentLanguage() {
        return mCurrentLanguage;
    }

    @Override
    public void invoke(SwingNode root) {
        SwingUtilities.invokeLater(() -> root.setContent(mTextScrollPane));
    }
}
