package app.controllers;

import app.model.*;

import app.net.HttpMethod;
import app.sockets.SocketManager;
import app.utils.Language;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TextField searchTextField;
    @FXML private ListView<Request> requestsListView;

    @FXML private TextField httpRequestTextField;
    @FXML private ComboBox<HttpMethod> httpReqComboBox;
    @FXML private Button sendRequestButton;

    @FXML private Label statusLabel;
    @FXML private Label timeLabel;

    @FXML private TabPane requestTabPane;
    @FXML private Tab requestParamsTab;
    @FXML private ListView<Attribute> requestParamsListView;
    @FXML private Tab requestHeadersTab;
    @FXML private ListView<Attribute> requestHeadersListView;
    @FXML private Tab requestBodyTab;

    @FXML private TabPane requestBodyTabPane;
    @FXML private ListView<Attribute> requestBodyDataListView;
    @FXML private ComboBox<Language> requestBodyRowComboBox;
    @FXML private Button requestBodyRowCopyButton;
    @FXML private Button requestBodyRowClearButton;

    @FXML private TabPane responseTabPane;
    @FXML private Tab responseBodyTab;
    @FXML private Tab responseHeadersTab;
    @FXML private ComboBox<Language> responseBodyComboBox;
    @FXML private Button responseBodyCopyButton;
    @FXML private Button responseBodyClearButton;

    @FXML private TextField socketUrlTextField;
    @FXML private Button socketConnectButton;
    @FXML private TextField socketEmitKey;
    @FXML private TextField socketEmitValue;
    @FXML private Button socketEmitButton;
    @FXML private ListView<Event> socketEventListView;
    @FXML private Button socketEventStartAll;
    @FXML private Button socketEventStopAll;
    @FXML private Button socketEventAddListener;

    private final SocketManager socketManager = new SocketManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListViews();
        setupComboBoxes();
        setupButtons();
    }

    private void setupListViews(){
        requestsListView.setCellFactory(studentListView -> new RequestListCell());
        requestParamsListView.setCellFactory(studentListView -> new AttributeListCell());
        requestHeadersListView .setCellFactory(studentListView -> new AttributeListCell());
        requestBodyDataListView.setCellFactory(studentListView -> new AttributeListCell());
        socketEventListView.setCellFactory(studentListView -> new EventListCell());
    }

    private void setupComboBoxes(){
        httpReqComboBox.getItems().setAll(HttpMethod.values());
        httpReqComboBox.getSelectionModel().select(0);

        requestBodyRowComboBox.getItems().setAll(Language.values());
        requestBodyRowComboBox.getSelectionModel().select(0);

        responseBodyComboBox.getItems().setAll(Language.values());
        responseBodyComboBox.getSelectionModel().select(0);
    }

    private void setupButtons(){
        socketConnectButton.setOnMouseClicked(event -> socketConnectButtonAction());
    }

    private void socketConnectButtonAction(){
        String socketUrl = socketUrlTextField.getText().trim();
        if (socketUrl.isEmpty()) {
            System.out.println("Invalid url");
            return;
        }

        if (socketManager.isSocketConnected()) {
            socketManager.disconnectSocket();
            socketUrlTextField.setEditable(true);
            socketConnectButton.setText("Connected");
        } else {
            socketManager.connectSocket(socketUrl, () -> {
                socketUrlTextField.setEditable(false);
                socketConnectButton.setText("Disconnected");
            });
        }
    }
}
