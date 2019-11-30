package app.utils;

import javafx.application.Platform;
import javafx.scene.Scene;

public class ThemeManager {

    public static void setTheme(Scene stage, Theme theme) {
        switch (theme){
            case DARK:
                setDarkTheme(stage);
                break;
            case WHITE:
                setWhiteTheme(stage);
                break;
        }
    }

    public static void setDarkTheme(Scene scene){
        Platform.runLater(() -> {
            scene.getStylesheets().remove("app/styles/white_theme.css");
            scene.getStylesheets().add("app/styles/dark_theme.css");
        });
    }

    public static void setWhiteTheme(Scene scene){
        Platform.runLater(() -> {
            scene.getStylesheets().add("app/styles/white_theme.css");
            scene.getStylesheets().remove("app/styles/dark_theme.css");
        });
    }
}
