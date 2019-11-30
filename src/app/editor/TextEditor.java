package app.editor;

import app.utils.SwingComponent;
import app.utils.Theme;
import javafx.embed.swing.SwingNode;
import org.fife.ui.rsyntaxtextarea.*;

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
        mTextArea.setHighlightCurrentLine(false);
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

    public void setEditorTheme(Theme theme){
        if(theme == Theme.DARK){
            setDarkTheme();
        }else{
            setWhiteTheme();
        }
    }

    public void setWhiteTheme() {
        mTextArea.setBackground(Color.white);
        SyntaxScheme ss = mTextArea.getSyntaxScheme();
        ss.setStyle(Token.SEPARATOR, new Style(Color.BLACK));
        ss.setStyle(Token.IDENTIFIER, new Style(Color.BLACK));
        // show double quotes / strings in dark cyan
        ss.setStyle(Token.LITERAL_STRING_DOUBLE_QUOTE,new Style(Color.red));
        // show attributes in RapidMiner orange
        ss.setStyle(Token.VARIABLE,new Style(Color.black));
        // show unknown attributes that are placed in brackets in [] in black
        ss.setStyle(Token.COMMENT_KEYWORD, new Style(Color.black));
        // show operators that are not defined in the functions in black (like other unknown words)
        ss.setStyle(Token.OPERATOR, new Style(Color.black));
        mTextArea.revalidate();
    }

    public void setDarkTheme() {
        mTextArea.setBackground(Color.decode("#282a36"));
        SyntaxScheme ss = mTextArea.getSyntaxScheme();
        ss.setStyle(Token.SEPARATOR, new Style(Color.WHITE));
        ss.setStyle(Token.IDENTIFIER, new Style(Color.WHITE));
        // show double quotes / strings in dark cyan
        ss.setStyle(Token.LITERAL_STRING_DOUBLE_QUOTE,new Style(Color.decode("#f1e669")));
        // show attributes in RapidMiner orange
        ss.setStyle(Token.VARIABLE,new Style(Color.decode("#50fa7b")));
        // show unknown attributes that are placed in brackets in [] in black
        ss.setStyle(Token.COMMENT_KEYWORD, new Style(Color.black));
        // show operators that are not defined in the functions in black (like other unknown words)
        ss.setStyle(Token.OPERATOR, new Style(Color.black));
        mTextArea.revalidate();
    }

    @Override
    public void invoke(SwingNode root) {
        SwingUtilities.invokeLater(() -> root.setContent(mTextScrollPane));
    }
}
