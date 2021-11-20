package sparkler.utils;

import javafx.application.Platform;
import javafx.scene.Scene;
import sparkler.model.AppInfo;

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
            scene.getStylesheets().remove(AppInfo.WHITE_STYLESHEET_PATH);
            scene.getStylesheets().add(AppInfo.DARK_STYLESHEET_PATH);
        });
    }

    public static void setWhiteTheme(Scene scene){
        Platform.runLater(() -> {
            scene.getStylesheets().remove(AppInfo.DARK_STYLESHEET_PATH);
            scene.getStylesheets().add(AppInfo.WHITE_STYLESHEET_PATH);
        });
    }

    public static String getThemePath(Theme theme){
        switch (theme){
            case DARK: return AppInfo.DARK_STYLESHEET_PATH;
            case WHITE: return AppInfo.WHITE_STYLESHEET_PATH;
        }
        return AppInfo.WHITE_STYLESHEET_PATH;
    }
}
