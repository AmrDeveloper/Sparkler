package app.net;

import app.editor.Language;

import java.util.List;
import java.util.Map;

public class HttpResponse {

    private int responseCode;
    private String responseBody;
    private Language contentType;
    private Map<String, List<String>> headers;

    public HttpResponse(int responseCode, String responseBody,
                        Language contentType,
                        Map<String, List<String>> headers) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.contentType = contentType;
        this.headers = headers;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Language getContentType() {
        return contentType;
    }

    public void setContentType(Language contentType) {
        this.contentType = contentType;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }
}
