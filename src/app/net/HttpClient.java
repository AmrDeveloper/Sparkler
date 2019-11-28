package app.net;

import app.editor.Language;
import app.utils.Log;
import app.utils.OnTimeoutChangeListener;
import app.utils.Settings;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HttpClient {

    private int connectTimeout = 10_000;
    private int writeTimeout = 30_000;
    private int readTimeout = 30_000;
    private OkHttpClient mHttpClient;
    private final Settings settings = new Settings();

    public HttpClient() {
        updateTimeoutFromPref();
        this.mHttpClient = initHttpClient();
    }

    private OkHttpClient initHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .build();
    }

    private void updateTimeoutFromPref(){
        this.connectTimeout = settings.getConnectTimeout();
        this.readTimeout = settings.getReadTimeout();
        this.writeTimeout = settings.getWriteTimeout();
        this.settings.setOnTimeoutChange(onTimeoutChangeListener);
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
                Language contentLanguage = Language.TEXT;
                if (contentType.contains("json")) {
                    contentLanguage = Language.JSON;
                } else if (contentType.contains("html")) {
                    contentLanguage = Language.HTML;
                } else if (contentType.contains("xml")) {
                    contentLanguage = Language.XML;
                }

                long requestAt = response.sentRequestAtMillis();
                long receiveAt = response.receivedResponseAtMillis();
                long requestTime = receiveAt - requestAt;

                HttpResponse httpResponse = new HttpResponse(responseCode, requestTime, responseBody, contentLanguage, headersMap);
                listener.onRequestSuccessful(httpResponse);
            }
        });
    }

    private Request createHttpClientRequest(HttpRequest request) {
        Request.Builder requestBuilder = new Request.Builder();

        if (request.getRequestParams() != null) {
            //Bind Parameters
            String requestUrl = bindQueryParameter(request.getRequestUrl(), request.getRequestParams());
            requestBuilder = requestBuilder.url(requestUrl);
            Log.info("Url Yes", requestUrl);
        } else {
            requestBuilder = requestBuilder.url(request.getRequestUrl());
            Log.info("Url No", request.getRequestUrl());
        }

        if (request.getRequestHeadersMap() != null) {
            //Bind Headers
            requestBuilder = bindRequestHeaders(requestBuilder, request.getRequestHeadersMap());
        }

        RequestBody requestBody;
        if (request.getRequestBodyMap() != null) {
            //Bind Body Map
            requestBody = bindRequestBody(request.getRequestBodyMap());
        }else{
            //Bind Body Text
            String bodyTxt = request.getRequestBody();
            MediaType mediaType;
            switch (request.getBodyContentType()){
                case JSON:
                    mediaType = MediaType.parse("application/json; charset=utf-8");
                    break;
                case XML:
                    mediaType = MediaType.parse("application/xml; charset=utf-8");
                    break;
                case HTML:
                    mediaType = MediaType.parse("text/html");
                    break;
                default:
                    mediaType = MediaType.parse("text/plain");
                    break;
            }
            requestBody = RequestBody.create(mediaType, bodyTxt);
        }

        switch (request.getRequestMethod()) {
            case GET:
                requestBuilder = requestBuilder.get();
                break;
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

    private OnTimeoutChangeListener onTimeoutChangeListener = new OnTimeoutChangeListener() {
        @Override
        public void onConnectTimeChange(int time) {
            connectTimeout = time;
            mHttpClient = initHttpClient();
        }

        @Override
        public void onReadTimeChange(int time) {
            readTimeout = time;
            mHttpClient = initHttpClient();
        }

        @Override
        public void onWriteTimeChange(int time) {
            writeTimeout = time;
            mHttpClient = initHttpClient();
        }
    };
}