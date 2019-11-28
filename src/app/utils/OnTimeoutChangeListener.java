package app.utils;

public interface OnTimeoutChangeListener {

    public void onConnectTimeChange(int time);

    public void onReadTimeChange(int time);

    public void onWriteTimeChange(int time);
}
