package sparkler.model;

public enum SparklerScreen {

    HTTP_REQUEST_SCREEN("/sparkler/views/http_view.fxml"),
    SOCKET_REQUEST_SCREEN("/sparkler/views/sockets_view.fxml"),
    ENVIRONMENT_SCREEN("/sparkler/views/environment_view.fxml"),
    SETTINGS_SCREEN("/sparkler/views/settings_view.fxml");

    private final String screenRelativePath;

    SparklerScreen(String path) {
        screenRelativePath = path;
    }

    public String getScreenRelativePath() {
        return screenRelativePath;
    }
}
