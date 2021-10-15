package sparkler.utils;

import javafx.application.Platform;
import javafx.scene.Scene;

public class ThemeManager {

    public static final String MAIN_THEME_PATH = "sparkler/styles/main_theme.css";
    public static final String DARK_THEME_PATH = "sparkler/styles/dark_theme.css";
    public static final String WHITE_THEME_PATH = "sparkler/styles/white_theme.css";

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
            scene.getStylesheets().remove(WHITE_THEME_PATH);
            scene.getStylesheets().add(DARK_THEME_PATH);
        });
    }

    public static void setWhiteTheme(Scene scene){
        Platform.runLater(() -> {
            scene.getStylesheets().remove(DARK_THEME_PATH);
            scene.getStylesheets().add(WHITE_THEME_PATH);
        });
    }

    public static String getThemePath(Theme theme){
        switch (theme){
            case DARK: return DARK_THEME_PATH;
            case WHITE: return WHITE_THEME_PATH;
        }
        return WHITE_THEME_PATH;
    }
}
