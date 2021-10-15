package sparkler.net;

import sparkler.editor.Language;

import java.util.List;
import java.util.Map;

public class HttpResponse {

    private int responseCode;
    private long responseTime;
    private float responseSize;
    private String responseBody;
    private Language contentType;
    private Map<String, List<String>> headers;

    public HttpResponse(int responseCode,
                        long responseTime,
                        float responseSize,
                        String responseBody,
                        Language contentType,
                        Map<String, List<String>> headers) {
        this.responseCode = responseCode;
        this.responseTime = responseTime;
        this.responseSize =responseSize;
        this.responseBody = responseBody;
        this.contentType = contentType;
        this.headers = headers;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public float getResponseSize(){
        return responseSize;
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
