package sparkler.model;

import sparkler.net.HttpMethod;

public class Request {

    private String url;
    private HttpMethod method;

    public Request(String url, HttpMethod method) {
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return url;
    }
}
