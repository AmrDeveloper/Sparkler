package sparkler;

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

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/main_view.fxml"));
        primaryStage.setTitle("Sparkler");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/sparkler/res/sparkler_icon.png")));
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("sparkler/styles/main_theme.css");
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
