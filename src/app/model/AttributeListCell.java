package app.model;

import app.utils.SparklerListCell;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;

import java.awt.*;

public class AttributeListCell extends SparklerListCell<Attribute> {

    @FXML private AnchorPane attributeViewLayout;
    @FXML private CheckBox attributeCheckbox;
    @FXML private TextField attributeKeyField;
    @FXML private TextField attributeNameField;

    private static final String EVENT_VIEW_LAYOUT = "../views/attribute_view.fxml";

    public AttributeListCell() {
        super(EVENT_VIEW_LAYOUT);
    }

    @Override
    public void onViewUpdate(Attribute item) {
        setGraphic(attributeViewLayout);
    }
}
