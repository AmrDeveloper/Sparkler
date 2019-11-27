package app.net;

import java.util.List;
import java.util.Map;

public class HttpResponse {

    private int responseCode;
    private String responseBody;
    private String contentType;
    private Map<String, List<String>> headers;

    public HttpResponse(int responseCode, String responseBody,
                        String contentType,
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }
}
