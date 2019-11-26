package app.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class Intent extends IntentStorage {

    private static Intent mIntent;
    private static final String DEBUG_TAG = Intent.class.getSimpleName();
    private static final Logger debug = Logger.getLogger(DEBUG_TAG);

    synchronized public static Intent getIntent() {
        if (mIntent == null) {
            mIntent = new Intent();
        }
        return mIntent;
    }

    public <T> T showAnotherView(String viewLocation, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewLocation));
            Parent root = loader.load();
            T controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setAlwaysOnTop(true);
            //stage.getIcons().add(Astro.APP_ICON);
            stage.show();
            return controller;
        } catch (IOException e) {
            debug.warning("Can't Open location : " + viewLocation);
            return null;
        }
    }

    public <T> T showAnotherView(String viewLocation, String title, String style) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewLocation));
            Parent root = loader.load();
            T controller = loader.getController();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(style);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setAlwaysOnTop(true);
            //stage.getIcons().add(Astro.APP_ICON);
            stage.show();
            return controller;
        } catch (IOException e) {
            debug.warning("Can't Open location : " + viewLocation);
            return null;
        }
    }

    public <T> T showAnotherView(String viewLocation, String title, String style, boolean onTop) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewLocation));
            Parent root = loader.load();
            T controller = loader.getController();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(style);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setAlwaysOnTop(onTop);
            //stage.getIcons().add(Astro.APP_ICON);
            stage.show();
            return controller;
        } catch (IOException e) {
            debug.warning("Can't Open location : " + viewLocation);
            return null;
        }
    }


    //@Nullable
    public <T> T showAnotherView(String viewLocation, String title, Image icon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewLocation));
            Parent root = loader.load();
            T controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setAlwaysOnTop(true);
            stage.getIcons().add(icon);
            //stage.getIcons().add(Astro.APP_ICON);
            stage.show();
            return controller;
        } catch (IOException e) {
            debug.warning("Can't Open location : " + viewLocation);
            return null;
        }
    }

    public <T> T showAnotherView(String viewLocation, String title, String style, Image icon) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewLocation));
            Parent root = loader.load();
            T controller = loader.getController();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(style);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setAlwaysOnTop(true);
            stage.getIcons().add(icon);
            stage.show();
            return controller;
        } catch (IOException e) {
            debug.warning("Can't Open location : " + viewLocation);
            return null;
        }
    }

    public <T> T getViewController(String viewLocation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewLocation));
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            debug.warning("Can't Open location : " + viewLocation);
            return null;
        }
    }
}