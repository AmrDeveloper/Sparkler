package app.utils;

public class TextFormatter {

    public String format(String text, Language language) {
        switch (language) {
            case JSON: {
                return formatJSON(text);
            }
            case XML: {
                return formatXML(text);
            }
            case HTML: {
                return formatHtml(text);
            }
            default: {
                return text;
            }
        }
    }

    private String formatHtml(String text) {
        return text;
    }

    public String formatXML(String text) {
        XmlFormatter formatter = new XmlFormatter();
        return formatter.format(text);
    }

    private String formatJSON(String text) {
        return text;
    }
}
