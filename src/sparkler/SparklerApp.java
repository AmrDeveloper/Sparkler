package sparkler;

import sparkler.model.AppInfo;
import sparkler.utils.Settings;
import sparkler.utils.Theme;
import sparkler.utils.ThemeManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SparklerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(AppInfo.MAIN_VIEW_PATH));
        primaryStage.setTitle(AppInfo.APP_NAME);
        primaryStage.getIcons().add(new Image(SparklerApp.class.getResourceAsStream(AppInfo.APP_ICON)));
        Scene scene = new Scene(root, AppInfo.DEFAULT_WIDTH, AppInfo.DEFAULT_HEIGHT);
        scene.getStylesheets().add(AppInfo.MAIN_STYLESHEET_PATH);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();

        Settings settings = new Settings();
        ThemeManager.setTheme(scene, Theme.valueOf(settings.getTheme()));
        settings.setThemeChangeListener(theme -> ThemeManager.setTheme(scene, theme));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
