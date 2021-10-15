package sparkler.model;

import sparkler.utils.SparklerListCell;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class RequestListCell extends SparklerListCell<Request> {

    @FXML private AnchorPane requestViewLayout;
    @FXML private Label requestUrlLabel;
    @FXML private Label requestMethodLabel;

    private static final String REQUEST_VIEW_LAYOUT = "/sparkler/views/request_view.fxml";

    public RequestListCell() {
        super(REQUEST_VIEW_LAYOUT);
    }

    @Override
    public void onViewUpdate() {
        requestUrlLabel.setText(getItem().getUrl());
        requestMethodLabel.setText(getItem().getMethod().name());

        switch (getItem().getMethod()){
            case GET:
                //Green
                requestMethodLabel.setTextFill(Color.web("#a9f552", 0.8));
                break;
            case POST:
                //Orange
                requestMethodLabel.setTextFill(Color.web("#fac03a", 0.8));
                break;
            case DELETE:
                //Red
                requestMethodLabel.setTextFill(Color.web("#f66c4d", 0.8));
                break;
            case PUT:
                //Blue
                requestMethodLabel.setTextFill(Color.web("#3aebfa", 0.8));
                break;
            case PATCH:
                //Purple
                requestMethodLabel.setTextFill(Color.web("#dd3ef9", 0.8));
                break;
            case HEAD:
                //Green
                requestMethodLabel.setTextFill(Color.web("#a9f552", 0.8));
                break;
            case OPTIONS:
                //Blue
                requestMethodLabel.setTextFill(Color.web("#3aebfa", 0.8));
                break;
        }

        setGraphic(requestViewLayout);
    }
}
