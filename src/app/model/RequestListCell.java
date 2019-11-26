package app.model;

import app.utils.SparklerListCell;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class RequestListCell extends SparklerListCell<Request> {

    @FXML private AnchorPane requestViewLayout;
    @FXML private Label requestMethodLabel;
    @FXML private Label requestUrlLabel;

    private static final String REQUEST_VIEW_LAYOUT = "views/request_view.fxml";

    public RequestListCell() {
        super(REQUEST_VIEW_LAYOUT);
    }

    @Override
    public void onViewUpdate(Request request) {
        requestMethodLabel.setText(request.getMethod());
        requestUrlLabel.setText(request.getUrl());
        setGraphic(requestViewLayout);
    }
}
