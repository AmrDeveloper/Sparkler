package sparkler.editor;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonFormatter extends Formatter {

    @Override
    public String format(String text) {
        String result = "";
        try {
            result = new JSONObject(text).toString(4);
        } catch (JSONException e) {
            result = text;
        }
        return result;
    }
}
