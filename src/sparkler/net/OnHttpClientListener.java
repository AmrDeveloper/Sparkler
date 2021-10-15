package sparkler.net;

public interface OnHttpClientListener {

    public void onRequestFailure();

    public void onRequestSuccessful(HttpResponse response);

}
