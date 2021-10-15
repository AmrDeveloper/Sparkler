package sparkler.net;

import sparkler.editor.Language;

import java.util.Map;

public class HttpRequest {

    private String requestUrl;
    private Map<String, String> requestParams;
    private Map<String, String> requestBodyMap;
    private Map<String, String> requestHeadersMap;
    private String requestBody;
    private Language bodyContentType;
    private HttpMethod requestMethod;

    public HttpRequest(String requestUrl, HttpMethod method){
        this.requestUrl = requestUrl;
        this.requestMethod = method;
        this.bodyContentType = Language.TEXT;
    }

    public HttpRequest(String requestUrl, Map<String, String> requestParams,
                       Map<String, String> requestBodyMap,
                       Map<String, String> requestHeadersMap,
                       String requestBody, Language bodyContentType,
                       HttpMethod requestMethod) {
        this.requestUrl = requestUrl;
        this.requestParams = requestParams;
        this.requestBodyMap = requestBodyMap;
        this.requestHeadersMap = requestHeadersMap;
        this.requestBody = requestBody;
        this.bodyContentType = bodyContentType;
        this.requestMethod = requestMethod;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void setRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
    }

    public void setRequestBodyMap(Map<String, String> requestBodyMap) {
        this.requestBodyMap = requestBodyMap;
    }

    public void setRequestHeaders(Map<String, String> requestHeadersMap) {
        this.requestHeadersMap = requestHeadersMap;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setBodyContentType(Language bodyContentType) {
        this.bodyContentType = bodyContentType;
    }

    public void setRequestMethod(HttpMethod requestMethod) {
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

    public Language getBodyContentType() {
        return bodyContentType;
    }

    public HttpMethod getRequestMethod() {
        return requestMethod;
    }
}
