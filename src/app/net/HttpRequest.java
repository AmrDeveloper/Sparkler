package app.net;

import java.util.Map;

public class HttpRequest {

    private String requestUrl;
    private Map<String, String> requestParams;
    private Map<String, String> requestBodyMap;
    private Map<String, String> requestHeadersMap;
    private String requestBody;
    private String bodyContentType;
    private HttpMethod requestMethod;

    public HttpRequest(String requestUrl, HttpMethod method){
        this.requestUrl = requestUrl;
        this.requestMethod = method;
    }

    public HttpRequest(String requestUrl, Map<String, String> requestParams,
                       Map<String, String> requestBodyMap,
                       Map<String, String> requestHeadersMap,
                       String requestBody, String bodyContentType,
                       HttpMethod requestMethod) {
        this.requestUrl = requestUrl;
        this.requestParams = requestParams;
        this.requestBodyMap = requestBodyMap;
        this.requestHeadersMap = requestHeadersMap;
        this.requestBody = requestBody;
        this.bodyContentType = bodyContentType;
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public Map<String, String> getRequestBodyMap() {
        return requestBodyMap;
    }

    public Map<String, String> getRequestHeadersMap() {
        return requestHeadersMap;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getBodyContentType() {
        return bodyContentType;
    }

    public HttpMethod getRequestMethod() {
        return requestMethod;
    }
}