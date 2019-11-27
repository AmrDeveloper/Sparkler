package app.controllers;

import app.model.*;

import app.net.HttpMethod;
import app.sockets.SocketManager;
import app.utils.ClipboardUtils;
import app.utils.Log;
import app.utils.Language;
import app.utils.TextEditor;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    @FXML private SwingNode requestBodyNode;
    @FXML private Button requestBodyRowCopyButton;
    @FXML private Button requestBodyRowClearButton;

    @FXML private TabPane responseTabPane;
    @FXML private Tab responseBodyTab;
    @FXML private Tab responseHeadersTab;
    @FXML private SwingNode responseBodyNode;
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

    private TextEditor requestBodyEditor;
    private TextEditor responseBodyEditor;
    private final SocketManager mSocketManager = SocketManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEditors();
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
        requestBodyRowComboBox.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> requestBodyEditor.changeLanguage(newValue));

        responseBodyComboBox.getItems().setAll(Language.values());
        responseBodyComboBox.getSelectionModel().select(0);
        responseBodyComboBox.getSelectionModel()
                .selectedItemProperty().addListener((observable, oldValue, newValue) -> responseBodyEditor.changeLanguage(newValue));
    }

    private void setupButtons(){
        //Copy and clear actions
        requestBodyRowCopyButton.setOnMouseClicked(event -> requestBodyRowCopyAction());
        requestBodyRowClearButton.setOnMouseClicked(event -> requestBodyRowClearAction());
        responseBodyCopyButton.setOnMouseClicked(event -> responseBodyCopyAction());
        responseBodyClearButton.setOnMouseClicked(event -> responseBodyClearAction());

        //Socket Views
        socketConnectButton.setOnMouseClicked(event -> socketConnectButtonAction());
        socketEmitButton.setOnMouseClicked(event -> socketEmitButtonAction());
        socketEventAddListener.setOnMouseClicked(event -> addEventSocketButton());
    }

    private void setupEditors() {
        requestBodyEditor = new TextEditor();
        requestBodyEditor.invoke(requestBodyNode);

        responseBodyEditor = new TextEditor();
        responseBodyEditor.invoke(responseBodyNode);
        responseBodyEditor.setEditable(false);
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

    private void requestBodyRowCopyAction(){
        String text = requestBodyEditor.getText().trim();
        ClipboardUtils.copyText(text);
    }

    private void requestBodyRowClearAction(){
        requestBodyEditor.clearText();
    }

    private void responseBodyCopyAction() {
        String text = responseBodyEditor.getText().trim();
        ClipboardUtils.copyText(text);
    }

    private void responseBodyClearAction(){
        responseBodyEditor.clearText();
    }
}
