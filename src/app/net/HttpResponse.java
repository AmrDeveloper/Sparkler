package app.net;

import app.editor.Language;

import java.util.List;
import java.util.Map;

public class HttpResponse {

    private int responseCode;
    private long requestTime;
    private String responseBody;
    private Language contentType;
    private Map<String, List<String>> headers;

    public HttpResponse(int responseCode, long requestTime, String responseBody,
                        Language contentType,
                        Map<String, List<String>> headers) {
        this.responseCode = responseCode;
        this.requestTime = requestTime;
        this.responseBody = responseBody;
        this.contentType = contentType;
        this.headers = headers;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Language getContentType() {
        return contentType;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
