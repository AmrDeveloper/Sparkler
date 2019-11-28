package app.controllers;

import app.model.*;

import app.net.*;
import app.sockets.SocketManager;
import app.utils.ClipboardUtils;
import app.utils.Log;
import app.editor.Language;
import app.editor.TextEditor;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

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
    @FXML private ListView<Header> responseHeaderListView;
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
    private final HttpClient httpClient = new HttpClient();
    private final SocketManager mSocketManager = SocketManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEditors();
        setupListViews();
        setupComboBoxes();
        setupButtons();
    }

    private void setupListViews(){
        requestsListView.setCellFactory(list -> new RequestListCell());
        requestParamsListView.setCellFactory(list -> new AttributeListCell());
        requestHeadersListView .setCellFactory(list -> new AttributeListCell());
        requestBodyDataListView.setCellFactory(list -> new AttributeListCell());
        socketEventListView.setCellFactory(list -> new EventListCell());
        responseHeaderListView.setCellFactory(list -> new HeaderListCell());

        requestsListView.setOnMouseClicked(e -> onHistoryListClickAction());
        setupRequestParamsListView();
        setupRequestHeadersListView();
        setupRequestBodyDataListView();
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

    private void setupButtons() {
        sendRequestButton.setOnMouseClicked(e -> makeHttpRequestButton());

        //Copy and clear actions
        requestBodyRowCopyButton.setOnMouseClicked(event -> ClipboardUtils.copyEditorText(requestBodyEditor));
        requestBodyRowClearButton.setOnMouseClicked(event -> requestBodyEditor.clearText());
        responseBodyCopyButton.setOnMouseClicked(event -> ClipboardUtils.copyEditorText(responseBodyEditor));
        responseBodyClearButton.setOnMouseClicked(event -> responseBodyEditor.clearText());

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

    private void setupRequestParamsListView(){
        requestParamsListView.getItems().add(new Attribute("", ""));
        requestParamsListView.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER: {
                    requestParamsListView.getItems().add(new Attribute("", ""));
                    break;
                }
                case DELETE: {
                    int selectedIndex = requestParamsListView.getSelectionModel().getSelectedIndex();
                    if (selectedIndex != -1) {
                        requestParamsListView.getItems().remove(selectedIndex);
                    }
                    break;
                }
            }
        });
    }

    private void setupRequestHeadersListView(){
        requestHeadersListView.getItems().add(new Attribute("", ""));
        requestHeadersListView.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER: {
                    requestHeadersListView.getItems().add(new Attribute("", ""));
                    break;
                }
                case DELETE: {
                    int selectedIndex = requestHeadersListView.getSelectionModel().getSelectedIndex();
                    if (selectedIndex != -1) {
                        requestHeadersListView.getItems().remove(selectedIndex);
                    }
                    break;
                }
            }
        });
    }

    private void setupRequestBodyDataListView(){
        requestBodyDataListView.getItems().add(new Attribute("", ""));
        requestBodyDataListView.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER: {
                    requestBodyDataListView.getItems().add(new Attribute("", ""));
                    break;
                }
                case DELETE: {
                    int selectedIndex = requestBodyDataListView.getSelectionModel().getSelectedIndex();
                    if (selectedIndex != -1) {
                        requestBodyDataListView.getItems().remove(selectedIndex);
                    }
                    break;
                }
            }
        });
    }

    private void onHistoryListClickAction() {
        Request currentRequest = requestsListView.getSelectionModel().getSelectedItem();
        if (currentRequest == null) {
            return;
        }
        httpRequestTextField.setText(currentRequest.getUrl());
        httpReqComboBox.getSelectionModel().select(currentRequest.getMethod());
    }

    private void makeHttpRequestButton(){
        String requestUrl = httpRequestTextField.getText();
        if (requestUrl.isEmpty()) {
            //TODO : will show dialog later
            return;
        }

        HttpRequest request = new HttpRequest(requestUrl, httpReqComboBox.getValue());

        boolean isParamsListEmpty = requestParamsListView.getItems().isEmpty();
        if (!isParamsListEmpty) {
            parseRequestParams(request);
        }

        boolean isHeadersListEmpty = requestHeadersListView.getItems().isEmpty();
        if(!isHeadersListEmpty){
            parseRequestHeaders(request);
        }

        boolean isBodyListEmpty = requestBodyDataListView.getItems().isEmpty();
        if(!isBodyListEmpty){
            parseRequestBody(request);
        }

        String requestBodyText = requestBodyEditor.getText();
        if(requestBodyText != null && !requestBodyText.isEmpty()){
            request.setRequestBody(requestBodyText);
        }

        //Set body content type
        Language bodyLanguage = requestBodyRowComboBox.getValue();
        request.setBodyContentType(bodyLanguage);

        httpClient.makeHttpRequest(request, new OnHttpClientListener() {
            @Override
            public void onRequestFailure() {
                Platform.runLater(() -> {
                    statusLabel.setText("Status : 404");
                });
            }

            @Override
            public void onRequestSuccessful(HttpResponse response) {
                Platform.runLater(() -> {
                    //Update response Code
                    String responseCode = "Status : " + response.getResponseCode();
                    statusLabel.setText(responseCode);

                    String time = "Time : " + response.getRequestTime() + "ms";
                    timeLabel.setText(time);

                    //Update Response Body
                    String responseBody = response.getResponseBody();
                    responseBodyEditor.changeLanguage(response.getContentType());
                    responseBodyEditor.setFormattedText(responseBody);

                    //Update headers
                    Map<String, List<String>> headers = response.getHeaders();
                    bindHeaderListView(headers);

                    //Update Language
                    responseBodyComboBox.getSelectionModel().select(response.getContentType());

                    //Insert this request to history
                    Request history = new Request(request.getRequestUrl(), request.getRequestMethod());
                    requestsListView.getItems().add(history);
                });
            }
        });
    }

    private void parseRequestParams(HttpRequest request){
        List<Attribute> paramsList = new ArrayList<>(requestParamsListView.getItems());
        Map<String, String> paramsMap = new HashMap<>(paramsList.size());
        for (Attribute attribute : paramsList) {
            if (attribute.getKey().isEmpty() ||
                    attribute.getValue().isEmpty() ||
                    !attribute.isUserChoice()) {
                continue;
            }
            paramsMap.putIfAbsent(attribute.getKey(), attribute.getValue());
        }
        if (!paramsMap.isEmpty()) {
            System.out.println(paramsMap);
            request.setRequestParams(paramsMap);
        }
    }

    private void parseRequestHeaders(HttpRequest request){
        List<Attribute> headerList = new ArrayList<>(requestHeadersListView.getItems());
        Map<String, String> headerMap = new HashMap<>(headerList.size());
        for (Attribute attribute : headerList) {
            if (attribute.getKey().isEmpty() ||
                    attribute.getValue().isEmpty() ||
                    !attribute.isUserChoice()) {
                continue;
            }
            headerMap.putIfAbsent(attribute.getKey(), attribute.getValue());
        }
        if (!headerMap.isEmpty()) {
            request.setRequestHeaders(headerMap);
        }
    }

    private void parseRequestBody(HttpRequest request){
        List<Attribute> bodyList = new ArrayList<>(requestBodyDataListView.getItems());
        Map<String, String> bodyMap = new HashMap<>(bodyList.size());
        for (Attribute attribute : bodyList) {
            if (attribute.getKey().isEmpty() ||
                    attribute.getValue().isEmpty() ||
                    !attribute.isUserChoice()) {
                continue;
            }
            bodyMap.putIfAbsent(attribute.getKey(), attribute.getValue());
        }
        if (!bodyMap.isEmpty()) {
            request.setRequestBodyMap(bodyMap);
        }
    }

    private void bindHeaderListView(Map<String, List<String>> headers){
        for (String key : headers.keySet()) {
            List<String> values = headers.get(key);
            if (values.size() == 1) {
                responseHeaderListView.getItems().add(new Header(key, values.get(0)));
            } else {
                responseHeaderListView.getItems().add(new Header(key, values.toString()));
            }
        }
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
