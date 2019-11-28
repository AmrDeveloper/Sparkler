package app.model;

import app.utils.SparklerListCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    public void onViewUpdate(Attribute attribute) {
        attributeKeyField.setText(attribute.getKey());
        attributeNameField.setText(attribute.getValue());

        attributeKeyField.textProperty().addListener((observable, oldValue, newValue) -> {
            attribute.setKey(newValue);
        });

        attributeNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            attribute.setValue(newValue);
        });

        attributeCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            attribute.setUserChoice(newValue);
        });

        setGraphic(attributeViewLayout);
    }
}
