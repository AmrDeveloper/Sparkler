package app.controllers;

import app.editor.EditorSearch;
import app.model.*;

import app.net.*;
import app.sockets.SocketManager;
import app.utils.*;
import app.editor.Language;
import app.editor.TextEditor;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML private TextField searchTextField;
    @FXML private ListView<Request> requestsListView;

    @FXML private TextField httpRequestTextField;
    @FXML private ComboBox<HttpMethod> httpReqComboBox;
    @FXML private JFXButton sendRequestButton;

    @FXML private Label statusLabel;
    @FXML private Label timeLabel;
    @FXML private Label sizeLabel;

    @FXML private AnchorPane responseSearchLayout;
    @FXML private ImageView searchIcon;
    @FXML private TextField resSearchField;
    @FXML private ImageView findNextResMatch;
    @FXML private ImageView findPrevResMatch;

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
    @FXML private JFXButton socketConnectButton;
    @FXML private TextField socketEmitKey;
    @FXML private TextField socketEmitValue;
    @FXML private JFXButton socketEmitButton;
    @FXML private ListView<Event> socketEventListView;
    @FXML private JFXButton socketEventAddListener;

    @FXML private MenuItem exitMenu;
    @FXML private MenuItem settingMenu;
    @FXML private MenuItem aboutMenu;

    @FXML private HBox spinnerLayout;

    private TextEditor requestBodyEditor;
    private TextEditor responseBodyEditor;
    private EditorSearch responseEditorSearch;

    private final Settings settings = new Settings();
    private final HttpClient httpClient = new HttpClient();
    private final SocketManager mSocketManager = SocketManager.getInstance();

    private ObservableList<Request> mRequestHistoryList = FXCollections.observableArrayList();
    private FilteredList<Request> mRequestFilteredList = new FilteredList<>(mRequestHistoryList, s -> true);

    private boolean isResponseSearchOpened = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinnerLayout.setVisible(false);
        setupEditors();
        setupListViews();
        setupComboBoxes();
        setupButtons();
        setupMenus();
        historySearchSetup();
        setupResponseSearch();
        settings.setThemeChangeListener(onThemeChangeListener);

        this.responseEditorSearch = new EditorSearch(responseBodyEditor.getTextArea());
    }

    private void setupListViews(){
        requestsListView.setItems(mRequestFilteredList);
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
        Theme currentTheme = Theme.valueOf(settings.getTheme());

        requestBodyEditor = new TextEditor();
        requestBodyEditor.setEditorTheme(currentTheme);
        requestBodyEditor.invoke(requestBodyNode);

        responseBodyEditor = new TextEditor();
        responseBodyEditor.setEditorTheme(currentTheme);
        responseBodyEditor.invoke(responseBodyNode);
        responseBodyEditor.setEditable(false);
    }

    private void setupMenus(){
        exitMenu.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        settingMenu.setOnAction(e -> {
            Theme currentTheme = Theme.valueOf(settings.getTheme());
            Intent intent = Intent.getIntent();
            String settingsView = "../views/settings_view.fxml";
            String themePath = ThemeManager.getThemePath(currentTheme);
            intent.showAnotherView(settingsView, "Settings", themePath, new Image("/app/res/sparkler_icon.png"));
        });

        aboutMenu.setOnAction(e -> {

        });
    }

    private void historySearchSetup(){
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.isEmpty()){
                mRequestFilteredList.setPredicate(s -> s.getUrl().contains(newValue));
            }else{
                mRequestFilteredList.setPredicate(s -> true);
            }
        });
    }

    private void setupRequestParamsListView() {
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

    private void setupRequestHeadersListView() {
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

    private void setupRequestBodyDataListView() {
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

    private void setupResponseSearch(){
        responseSearchLayout.setVisible(false);

        searchIcon.setOnMouseMoved(e -> {
            Tooltip.install(searchIcon, new Tooltip("Open response body search"));
        });

        searchIcon.setOnMouseClicked(e -> {
            if(isResponseSearchOpened){
                responseSearchLayout.setVisible(false);
            }else{
                responseSearchLayout.setVisible(true);
            }
            isResponseSearchOpened = !isResponseSearchOpened;
        });

        resSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()){
                return;
            }
            responseEditorSearch.startSearch(newValue);
        });

        findNextResMatch.setOnMouseClicked(e -> Platform.runLater(() -> responseEditorSearch.searchNextMatch()));
        findPrevResMatch.setOnMouseClicked(e -> Platform.runLater(() -> responseEditorSearch.searchPrevMath()));
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

        spinnerLayout.setVisible(true);

        httpClient.makeHttpRequest(request, new OnHttpClientListener() {
            @Override
            public void onRequestFailure() {
                Platform.runLater(() -> {
                    statusLabel.setText("Status : 404");
                    spinnerLayout.setVisible(false);
                });
            }

            @Override
            public void onRequestSuccessful(HttpResponse response) {
                Platform.runLater(() -> {
                    spinnerLayout.setVisible(false);
                    //Update response Code
                    String responseCode = "Status : " + response.getResponseCode();
                    statusLabel.setText(responseCode);

                    String time = "Time : " + response.getResponseTime() + "ms";
                    timeLabel.setText(time);

                    String size = "Size : " + String.format("%.2f", response.getResponseSize()) + "KB";
                    sizeLabel.setText(size);

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
                    mRequestHistoryList.add(history);
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

    private final OnThemeChangeListener onThemeChangeListener = (theme) -> {
        requestBodyEditor.setEditorTheme(theme);
        responseBodyEditor.setEditorTheme(theme);
    };
}
