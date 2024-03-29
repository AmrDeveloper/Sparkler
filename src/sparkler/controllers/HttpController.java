package sparkler.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import sparkler.utils.Environment;
import sparkler.editor.EditorSearch;
import sparkler.editor.Language;
import sparkler.editor.TextEditor;
import sparkler.model.*;
import sparkler.net.*;
import sparkler.utils.ClipboardUtils;
import sparkler.utils.Settings;
import sparkler.utils.Theme;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpController implements Initializable {

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

    @FXML private ListView<Attribute> requestParamsListView;
    @FXML private ListView<Attribute> requestHeadersListView;
    @FXML private ListView<Attribute> requestBodyDataListView;
    @FXML private ComboBox<Language> requestBodyRowComboBox;

    @FXML private SwingNode requestBodyNode;
    @FXML private Button requestBodyRowCopyButton;
    @FXML private Button requestBodyRowClearButton;

    @FXML private ListView<Header> responseHeaderListView;
    @FXML private SwingNode responseBodyNode;
    @FXML private ComboBox<Language> responseBodyComboBox;
    @FXML private Button responseBodyCopyButton;
    @FXML private Button responseBodyClearButton;

    @FXML private HBox spinnerLayout;

    private TextEditor requestBodyEditor;
    private TextEditor responseBodyEditor;
    private EditorSearch responseEditorSearch;

    private boolean isResponseSearchOpened = false;

    private final Settings settings = new Settings();
    private final HttpClient httpClient = new HttpClient();
    private final Environment environment = new Environment();

    private static final Pattern attributePattern = Pattern.compile("\\{\\{(?<ATTRIBUTE>[a-zA-Z_]+)}}");

    private final ObservableList<Request> mRequestHistoryList = FXCollections.observableArrayList();
    private final FilteredList<Request> mRequestFilteredList = new FilteredList<>(mRequestHistoryList, s -> true);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinnerLayout.setVisible(false);

        setupViewsTooltip();
        setupEditors();
        setupListViews();
        setupComboBoxes();
        setupButtons();
        historySearchSetup();
        setupResponseSearch();

        this.responseEditorSearch = new EditorSearch(responseBodyEditor.getTextArea());
    }

    private void setupViewsTooltip() {
        Tooltip.install(sendRequestButton, new Tooltip("Send Request to the server"));
        Tooltip.install(responseBodyCopyButton, new Tooltip("Copy response body"));
        Tooltip.install(responseBodyClearButton, new Tooltip("Clear response body"));
        Tooltip.install(searchIcon, new Tooltip("Open response body search"));
    }

    private void setupListViews(){
        requestsListView.setItems(mRequestFilteredList);
        requestsListView.setCellFactory(list -> new RequestListCell());
        requestParamsListView.setCellFactory(list -> new AttributeListCell());
        requestHeadersListView .setCellFactory(list -> new AttributeListCell());
        requestBodyDataListView.setCellFactory(list -> new AttributeListCell());
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
        requestBodyRowCopyButton.setOnMouseClicked(event -> ClipboardUtils.copyEditorText(requestBodyEditor));
        requestBodyRowClearButton.setOnMouseClicked(event -> requestBodyEditor.clearText());
        responseBodyCopyButton.setOnMouseClicked(event -> ClipboardUtils.copyEditorText(responseBodyEditor));
        responseBodyClearButton.setOnMouseClicked(event -> responseBodyEditor.clearText());
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

    private void historySearchSetup(){
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                mRequestFilteredList.setPredicate(s -> s.getUrl().contains(newValue));
            } else {
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
        searchIcon.setOnMouseClicked(e -> {
            responseSearchLayout.setVisible(!isResponseSearchOpened);
            isResponseSearchOpened = !isResponseSearchOpened;
        });

        resSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) return;
            responseEditorSearch.startSearch(newValue);
        });

        findNextResMatch.setOnMouseClicked(e -> Platform.runLater(() -> responseEditorSearch.searchNextMatch()));
        findPrevResMatch.setOnMouseClicked(e -> Platform.runLater(() -> responseEditorSearch.searchPrevMatch()));
    }

    private void onHistoryListClickAction() {
        Request currentRequest = requestsListView.getSelectionModel().getSelectedItem();
        if (currentRequest == null) return;
        httpRequestTextField.setText(currentRequest.getUrl());
        httpReqComboBox.getSelectionModel().select(currentRequest.getMethod());
    }

    private void makeHttpRequestButton(){
        String requestUrl = httpRequestTextField.getText();
        if (requestUrl.isEmpty()) {
            return;
        }

        requestUrl = preprocessorRequestUrl(requestUrl);

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

        httpClient.makeHttpRequest(request, response -> {
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
                bindHeaderListView(response.getHeaders());

                //Update Language
                responseBodyComboBox.getSelectionModel().select(response.getContentType());

                //Insert this request to history
                Request history = new Request(request.getRequestUrl(), request.getRequestMethod());
                mRequestHistoryList.add(history);
            });
        }, exception -> {
            Platform.runLater(() -> {
                spinnerLayout.setVisible(false);
                statusLabel.setText("Status : 404");
            });
        });
    }

    private String preprocessorRequestUrl(String url) {
        Matcher matcher = attributePattern.matcher(url);
        String processedUrl = url;
        while (matcher.find()) {
            String attributeName = matcher.group("ATTRIBUTE");
            String attributeValue = environment.getActiveVariableValue(attributeName);
            if (!attributeValue.isEmpty()) {
                processedUrl = processedUrl.replaceAll("\\{\\{" + attributeName + "}}", attributeValue);
            }
        }
        return processedUrl;
    }

    private void parseRequestParams(HttpRequest request){
        Map<String, String> paramsMap = new HashMap<>();
        for (Attribute attribute : requestParamsListView.getItems()) {
            if (attribute.getKey().isEmpty() || attribute.getValue().isEmpty() || !attribute.isUserChoice()) {
                continue;
            }
            paramsMap.putIfAbsent(attribute.getKey(), attribute.getValue());
        }
        if (paramsMap.isEmpty()) return;
        request.setRequestParams(paramsMap);
    }

    private void parseRequestHeaders(HttpRequest request){
        Map<String, String> headerMap = new HashMap<>();
        for (Attribute attribute : requestHeadersListView.getItems()) {
            if (attribute.getKey().isEmpty() || attribute.getValue().isEmpty() || !attribute.isUserChoice()) {
                continue;
            }
            headerMap.putIfAbsent(attribute.getKey(), attribute.getValue());
        }
        if (headerMap.isEmpty()) return;
        request.setRequestHeaders(headerMap);
    }

    private void parseRequestBody(HttpRequest request){
        List<Attribute> bodyList = new ArrayList<>(requestBodyDataListView.getItems());
        Map<String, String> bodyMap = new HashMap<>(bodyList.size());
        for (Attribute attribute : bodyList) {
            if (attribute.getKey().isEmpty() || attribute.getValue().isEmpty() || !attribute.isUserChoice()) {
                continue;
            }
            bodyMap.putIfAbsent(attribute.getKey(), attribute.getValue());
        }
        if (bodyMap.isEmpty()) return;
        request.setRequestBodyMap(bodyMap);
    }

    private void bindHeaderListView(Map<String, List<String>> headers){
        for (String key : headers.keySet()) {
            List<String> values = headers.get(key);
            Header header = values.size() == 1 ? new Header(key, values.get(0)) : new Header(key, values.toString());
            responseHeaderListView.getItems().add(header);
        }
    }
}
