package sparkler.controllers;

import sparkler.utils.Log;
import sparkler.utils.Settings;
import sparkler.utils.Theme;
import sparkler.utils.Validation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML private JFXButton settingsSaveButton;
    @FXML private JFXToggleButton themeToggleButton;
    @FXML private TextField connectTimeoutValue;
    @FXML private TextField readTimeoutValue;
    @FXML private TextField writeTimeoutValue;

    private final Settings settings = new Settings();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showCurrentValues();

        settingsSaveButton.setOnMouseClicked(e -> {
            String connectTimeoutTxt = connectTimeoutValue.getText();
            if (!Validation.isValidNumber(connectTimeoutTxt)) {
                 //TODO : Show dialog that is invalid number
                Log.warn("Setting", "Invalid Write timeout");
                return;
            }

            String readTimeoutTxt = readTimeoutValue.getText();
            if (!Validation.isValidNumber(readTimeoutTxt)) {
                 //TODO : Show dialog that is invalid number
                Log.warn("Setting", "Invalid Write timeout");
                return;
            }

            String writeTimeoutTxt = writeTimeoutValue.getText();
            if (!Validation.isValidNumber(writeTimeoutTxt)) {
                //TODO : Show dialog that is invalid number
                Log.warn("Setting", "Invalid Write timeout");
                return;
            }

            Theme theme = themeToggleButton.isSelected() ? Theme.DARK : Theme.WHITE;
            settings.setTheme(theme);

            settings.setConnectTimeout(Integer.parseInt(connectTimeoutTxt));
            settings.setReadTimeout(Integer.parseInt(readTimeoutTxt));
            settings.setWriteTimeout(Integer.parseInt(writeTimeoutTxt));
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
        boolean isDark = "DARK".equals(settings.getTheme());
        if(isDark) themeToggleButton.setSelected(true);
    }
}
