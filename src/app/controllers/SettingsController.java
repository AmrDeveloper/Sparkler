package app.controllers;

import app.utils.Settings;
import app.utils.Theme;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML private JFXButton settingsSaveButton;
    @FXML private JFXButton settingsCloseButton;
    @FXML private JFXToggleButton themeToggleButton;
    @FXML private TextField connectTimeoutValue;
    @FXML private TextField readTimeoutValue;
    @FXML private TextField writeTimeoutValue;

    private final Settings settings = new Settings();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showCurrentValues();

        settingsSaveButton.setOnMouseClicked(e -> {
            boolean isDark = themeToggleButton.isSelected();
            if(isDark){
                settings.setTheme(Theme.DARK);
            }else{
                settings.setTheme(Theme.WHITE);
            }
            //TODO : update times values
        });

        settingsCloseButton.setOnMouseClicked(e -> {
            Stage stage = (Stage) settingsCloseButton.getScene().getWindow();
            stage.close();
        });
    }

    private void showCurrentValues(){
        setupToggleThemeButton();

        int connectTimeout = settings.getConnectTimeout();
        int readmeTimeout = settings.getReadTimeout();
        int writeTimeout = settings.getWriteTimeout();

        connectTimeoutValue.setText(String.valueOf(connectTimeout));
        readTimeoutValue.setText(String.valueOf(readmeTimeout));
        writeTimeoutValue.setText(String.valueOf(writeTimeout));
    }

    private void setupToggleThemeButton(){
        boolean isDark = settings.getTheme().equals("DARK");
        if(isDark){
            themeToggleButton.setSelected(true);
        }
    }
}
