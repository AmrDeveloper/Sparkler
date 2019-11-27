package app.model;

import app.utils.SparklerListCell;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class HeaderListCell extends SparklerListCell<Header> {

    @FXML private AnchorPane responseHeaderLayout;
    @FXML private TextField responseHeaderKey;
    @FXML private TextField responseHeaderValue;

    private static final String HEADER_VIEW_LAYOUT = "../views/header_view.fxml";

    public HeaderListCell() {
        super(HEADER_VIEW_LAYOUT);
    }

    @Override
    public void onViewUpdate(Header header) {
        responseHeaderKey.setText(header.getKey());
        responseHeaderValue.setText(header.getValue());
        setGraphic(responseHeaderLayout);
    }
}
