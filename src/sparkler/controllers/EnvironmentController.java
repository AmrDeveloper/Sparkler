package sparkler.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import sparkler.utils.Environment;
import sparkler.model.Attribute;
import sparkler.model.AttributeListCell;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EnvironmentController implements Initializable {

    @FXML private Button addVariableButton;
    @FXML private Button saveVariablesButton;
    @FXML private ListView<Attribute> variablesListView;

    private final Environment environment = new Environment();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupViewsTooltip();
        setupListeners();
        loadAttributes();
    }

    private void setupViewsTooltip() {
        Tooltip.install(addVariableButton, new Tooltip("Add new Environment Variable"));
        Tooltip.install(saveVariablesButton, new Tooltip("Save Environment Variables"));
    }

    private void setupListeners() {
        variablesListView.setCellFactory(list -> new AttributeListCell());
        addVariableButton.setOnMouseClicked(e -> addNewAttribute());
        saveVariablesButton.setOnMouseClicked(e -> saveAttributes());
    }

    private void addNewAttribute() {
        variablesListView.getItems().add(new Attribute("",""));
    }

    private void loadAttributes() {
        List<Attribute> attributes = environment.loadEnvironmentAttributes();
        ObservableList<Attribute> attributeObservableList =  variablesListView.getItems();
        attributeObservableList.addAll(attributes);
    }

    private void saveAttributes() {
        ObservableList<Attribute> attributeObservableList = variablesListView.getItems();
        environment.updateEnvironmentAttributes(attributeObservableList);
    }
}
