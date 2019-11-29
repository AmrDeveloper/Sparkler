package app.model;

import app.utils.SparklerListCell;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
    public void onViewUpdate() {
        attributeKeyField.setText(getItem().getKey());
        attributeNameField.setText(getItem().getValue());
        attributeCheckbox.setSelected(getItem().isUserChoice());

        attributeKeyField.textProperty().addListener((observable, oldValue, newValue) -> {
            getItem().setKey(newValue);
        });

        attributeNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            getItem().setValue(newValue);
        });

        attributeCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            getItem().setUserChoice(newValue);
        });

        setGraphic(attributeViewLayout);
    }
}
