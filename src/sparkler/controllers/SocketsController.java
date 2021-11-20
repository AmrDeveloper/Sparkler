package sparkler.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import sparkler.model.Event;
import sparkler.model.EventListCell;
import sparkler.sockets.SocketManager;
import sparkler.utils.Log;

import java.net.URL;
import java.util.ResourceBundle;

public class SocketsController implements Initializable {

    @FXML private TextField socketUrlTextField;
    @FXML private JFXButton socketConnectButton;
    @FXML private TextField socketEmitKey;
    @FXML private TextField socketEmitValue;
    @FXML private JFXButton socketEmitButton;
    @FXML private ListView<Event> socketEventListView;
    @FXML private JFXButton socketEventAddListener;

    private final SocketManager mSocketManager = SocketManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListViews();
        setupButtons();
    }

    private void setupListViews() {
        socketEventListView.setCellFactory(list -> new EventListCell());
    }

    private void setupButtons() {
        socketConnectButton.setOnMouseClicked(event -> socketConnectButtonAction());
        socketEmitButton.setOnMouseClicked(event -> socketEmitButtonAction());
        socketEventAddListener.setOnMouseClicked(event -> addEventSocketButton());
    }

    private void socketConnectButtonAction(){
        String socketUrl = socketUrlTextField.getText().trim();
        if (socketUrl.isEmpty()) {
            Log.warn("Socket", "Invalid Socket Url");
            return;
        }

        if (mSocketManager.isSocketConnected()) {
            mSocketManager.disconnectSocket();
            socketUrlTextField.setEditable(true);
            socketConnectButton.setText("Connected");
            Log.info("Socket", "Socket Disconnected");
        } else {
            mSocketManager.connectSocket(socketUrl, () -> {
                socketUrlTextField.setEditable(false);
                socketConnectButton.setText("Disconnected");
                Log.info("Socket", "Socket Connected");
            });
        }
    }

    private void socketEmitButtonAction(){
        if(!mSocketManager.isSocketConnected()){
            Log.warn("Socket","Not Socket Connected");
            return;
        }

        String key = socketEmitKey.getText().trim();
        String value = socketEmitValue.getText().trim();

        if(key.isEmpty() || value.isEmpty()){
            Log.warn("Socket","Invalid Socket key or value");
            return;
        }

        mSocketManager.emitValue(key, value);
    }

    private void addEventSocketButton(){
        socketEventListView.getItems().add(new Event("",0,false));
    }
}
