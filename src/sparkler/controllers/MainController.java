package sparkler.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import sparkler.model.SparklerScreen;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML private ImageView httpViewAction;
    @FXML private ImageView socketViewAction;
    @FXML private ImageView environmentViewAction;
    @FXML private ImageView settingsViewAction;

    @FXML private BorderPane navigationBorderPane;

    private SparklerScreen currentAppScreen = SparklerScreen.HTTP_REQUEST_SCREEN;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupViewsTooltip();
        setupScreensChangeListener();

        loadViewIntoBorderPane(currentAppScreen, navigationBorderPane);
    }

    private void setupViewsTooltip() {
        Tooltip.install(httpViewAction, new Tooltip("Http/Https"));
        Tooltip.install(socketViewAction, new Tooltip("Sockets"));
        Tooltip.install(environmentViewAction, new Tooltip("Environment"));
        Tooltip.install(settingsViewAction, new Tooltip("Settings"));
    }

    private void setupScreensChangeListener() {
        httpViewAction.setOnMouseClicked(e -> {
            if (currentAppScreen != SparklerScreen.HTTP_REQUEST_SCREEN) {
                currentAppScreen = SparklerScreen.HTTP_REQUEST_SCREEN;
                loadViewIntoBorderPane(currentAppScreen, navigationBorderPane);
            }
        });

        socketViewAction.setOnMouseClicked(e -> {
            if (currentAppScreen != SparklerScreen.SOCKET_REQUEST_SCREEN) {
                currentAppScreen = SparklerScreen.SOCKET_REQUEST_SCREEN;
                loadViewIntoBorderPane(currentAppScreen, navigationBorderPane);
            }
        });

        environmentViewAction.setOnMouseClicked(e -> {
            if (currentAppScreen != SparklerScreen.ENVIRONMENT_SCREEN) {
                currentAppScreen = SparklerScreen.ENVIRONMENT_SCREEN;
                loadViewIntoBorderPane(currentAppScreen, navigationBorderPane);
            }
        });

        settingsViewAction.setOnMouseClicked(e -> {
            if (currentAppScreen != SparklerScreen.SETTINGS_SCREEN) {
                currentAppScreen = SparklerScreen.SETTINGS_SCREEN;
                loadViewIntoBorderPane(currentAppScreen, navigationBorderPane);
            }
        });
    }

    private void loadViewIntoBorderPane(SparklerScreen screen, BorderPane pane)  {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(screen.getScreenRelativePath()));
        } catch (IOException e) {
            System.err.println("Invalid View Loader : " + e.getMessage());
        }
        pane.setCenter(root);
    }
}
