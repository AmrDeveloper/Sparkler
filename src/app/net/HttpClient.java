package app.net;

import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HttpClient {

    private int connectTimeout = 10;
    private int writeTimeout = 10;
    private int readTimeout = 30;
    private final OkHttpClient mHttpClient;

    public HttpClient() {
        this.mHttpClient = initHttpClient();
    }

    private OkHttpClient initHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();
    }

    public void makeHttpRequest(HttpRequest request, OnHttpClientListener listener) {
        Request httpRequest = createHttpClientRequest(request);
        mHttpClient.newCall(httpRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onRequestFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int responseCode = response.code();
                String responseBody = response.body().string();
                Map<String, List<String>> headersMap = response.headers().toMultimap();
                String contentType = Objects.requireNonNull(response.header("Content-Type")).split(";")[0];
                HttpResponse httpResponse = new HttpResponse(responseCode, responseBody, contentType, headersMap);
                listener.onRequestSuccessful(httpResponse);
            }
        });
    }

    private Request createHttpClientRequest(HttpRequest request) {
        Request.Builder requestBuilder = new Request.Builder();

        if(request.getRequestParams() != null) {
            //Bind Parameters
            String requestUrl = bindQueryParameter(request.getRequestUrl(), request.getRequestParams());
            requestBuilder.url(requestUrl);
        }else{
            requestBuilder.url(request.getRequestUrl());
        }

        if(request.getRequestHeadersMap() != null) {
            //Bind Headers
            requestBuilder = bindRequestHeaders(requestBuilder, request.getRequestHeadersMap());
        }

        if(request.getRequestBodyMap() != null) {
            //Bind Body
            RequestBody requestBody = bindRequestBody(request.getRequestBodyMap());
            switch (request.getRequestMethod()){
                case POST:
                    requestBuilder = requestBuilder.post(requestBody);
                    break;
                case PUT:
                    requestBuilder = requestBuilder.put(requestBody);
                    break;
                case PATCH:
                    requestBuilder = requestBuilder.patch(requestBody);
                    break;
                case DELETE:
                    requestBuilder = requestBuilder.delete(requestBody);
                    break;
            }

        }else{
            switch (request.getRequestMethod()){
                case GET:
                    requestBuilder = requestBuilder.get();
                    break;
                case DELETE:
                    requestBuilder = requestBuilder.delete();
                    break;
            }
        }

        return requestBuilder.build();
    }

    private String bindQueryParameter(String requestUrl, Map<String, String> queryParams) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(requestUrl).newBuilder();
        for (String key : queryParams.keySet()) {
            urlBuilder.addQueryParameter(key, queryParams.get(key));
        }
        return urlBuilder.build().toString();
    }

    private Request.Builder bindRequestHeaders(Request.Builder builder, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            builder = builder.addHeader(key, headers.get(key));
        }
        return builder;
    }

    private RequestBody bindRequestBody(Map<String, String> bodyMap) {
        FormBody.Builder formBody = new FormBody.Builder();
        for (String key : bodyMap.keySet()) {
            formBody = formBody.add(key, bodyMap.get(key));
        }
        return formBody.build();
    }
}
