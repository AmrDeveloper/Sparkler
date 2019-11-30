package app;

import app.utils.Theme;
import javafx.stage.Stage;

public class ThemeManager {

    public static void setTheme(Stage stage, Theme theme) {
        switch (theme){
            case DARK:
                setDarkTheme(stage);
                break;
            case WHITE:
                setWhiteTheme(stage);
                break;
        }
    }

    public static void setDarkTheme(Stage stage){

    }

    public static void setWhiteTheme(Stage stage){

    }
}
