package app.sockets;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class SocketManager {

    private Socket mSocket;

    public SocketManager() {

    }

    public void initSocket(String socketUrl) {
        try {
            if (mSocket != null) {
                mSocket.close();
            }
            mSocket = IO.socket(socketUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectSocket(String socketUrl, SocketListener listener) {
        if (mSocket == null) {
            initSocket(socketUrl);
        }

        if (!mSocket.connected()) {
            mSocket.connect();
            listener.onEventSuccess();
        }
    }

    public void emitValue(String key, Object value) {
        mSocket.emit(key, value);
    }

    public void startSocketListener(String event, SocketListener listener) {
        mSocket.on(event, objects -> listener.onEventSuccess());
    }

    public void stopSocketListener(String event, SocketListener listener) {
        mSocket.off(event, objects -> listener.onEventSuccess());
    }

    public void disconnectSocket() {
        mSocket.disconnect();
    }

    public boolean isSocketConnected() {
        if (mSocket == null) {
            return false;
        }
        return mSocket.connected();
    }
}
