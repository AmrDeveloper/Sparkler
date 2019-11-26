package app.sockets;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

class SocketManager {

    private Socket mSocket;
    private boolean isSocketConnected;

    public SocketManager() {

    }

    public void initSocket(String socketUrl) {
        try {
            mSocket = IO.socket(socketUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectSocket() {
        if (!isSocketConnected) {
            mSocket.connect();
            isSocketConnected = true;
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
        isSocketConnected = false;
    }
}
