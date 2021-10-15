package sparkler.editor;

import sparkler.utils.SwingComponent;
import sparkler.utils.Theme;
import javafx.embed.swing.SwingNode;
import org.fife.ui.rsyntaxtextarea.*;

import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;

public class TextEditor extends SwingComponent {

    private RSyntaxTextArea mTextArea;
    private RTextScrollPane mTextScrollPane;

    private Gutter mTextAreaGutter;
    private Language mCurrentLanguage = Language.TEXT;
    private static final TextFormatter mFormatter = new TextFormatter();

    public TextEditor() {
        setupNewEditor();
        mTextAreaGutter = RSyntaxUtilities.getGutter(mTextArea);
    }

    private void setupNewEditor() {
        mTextArea = new RSyntaxTextArea();
        mTextArea.setHighlightCurrentLine(false);
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
        mTextArea.setCaretColor(Color.BLACK);
        mTextArea.setBackground(Color.white);
        mTextAreaGutter.setBackground(Color.white);

        SyntaxScheme syntaxScheme = mTextArea.getSyntaxScheme();
        syntaxScheme.setStyle(Token.SEPARATOR, new Style(Color.BLACK));
        syntaxScheme.setStyle(Token.IDENTIFIER, new Style(Color.BLACK));
        syntaxScheme.setStyle(Token.LITERAL_STRING_DOUBLE_QUOTE,new Style(Color.red));
        syntaxScheme.setStyle(Token.VARIABLE,new Style(Color.black));
        syntaxScheme.setStyle(Token.COMMENT_KEYWORD, new Style(Color.black));
        syntaxScheme.setStyle(Token.OPERATOR, new Style(Color.black));
        mTextArea.revalidate();
    }

    public void setDarkTheme() {
        mTextArea.setCaretColor(Color.LIGHT_GRAY);
        mTextArea.setBackground(Color.decode("#292a2d"));
        mTextAreaGutter.setBackground(Color.decode("#292a2d"));

        SyntaxScheme syntaxScheme = mTextArea.getSyntaxScheme();
        syntaxScheme.setStyle(Token.SEPARATOR, new Style(Color.LIGHT_GRAY));
        syntaxScheme.setStyle(Token.IDENTIFIER, new Style(Color.LIGHT_GRAY));
        syntaxScheme.setStyle(Token.VARIABLE,new Style(Color.decode("#50fa7b")));
        syntaxScheme.setStyle(Token.COMMENT_KEYWORD, new Style(Color.black));
        syntaxScheme.setStyle(Token.OPERATOR, new Style(Color.black));
        syntaxScheme.setStyle(Token.LITERAL_BOOLEAN, new Style(Color.decode("#ba47f0")));
        syntaxScheme.setStyle(Token.LITERAL_BACKQUOTE, new Style(Color.decode("#ba47f0")));
        syntaxScheme.setStyle(Token.LITERAL_CHAR, new Style(Color.decode("#ba47f0")));
        syntaxScheme.setStyle(Token.LITERAL_NUMBER_DECIMAL_INT, new Style(Color.decode("#ba47f0")));
        syntaxScheme.setStyle(Token.LITERAL_NUMBER_FLOAT, new Style(Color.decode("#ba47f0")));
        syntaxScheme.setStyle(Token.LITERAL_STRING_DOUBLE_QUOTE,new Style(Color.decode("#f1e669")));
        mTextArea.revalidate();
    }

    @Override
    public void invoke(SwingNode root) {
        SwingUtilities.invokeLater(() -> root.setContent(mTextScrollPane));
    }
}
