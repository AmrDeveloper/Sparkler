package app.model;

import app.sockets.SocketManager;
import app.utils.Log;
import app.utils.SparklerListCell;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class EventListCell extends SparklerListCell<Event> {

    @FXML private AnchorPane eventViewLayout;

    @FXML private TextField eventNameField;
    @FXML private Label listenCounterLabel;
    @FXML private Button listenerButton;

    private SocketManager mSocketManager = SocketManager.getInstance();
    private static final String EVENT_VIEW_LAYOUT = "../views/event_view.fxml";

    public EventListCell() {
        super(EVENT_VIEW_LAYOUT);
    }

    @Override
    public void onViewUpdate(Event event) {
        listenCounterLabel.setText(event.getNotifyNumber() + "");

        listenerButton.setOnMouseClicked(e -> {
            String eventName = eventNameField.getText();
            if (eventName.isEmpty() || !mSocketManager.isSocketConnected()) {
                Log.warn("EventCell", "Invalid Event Name");
                return;
            }
            event.setName(eventName);
            if (event.isListening()) {
                listenerButton.setText("Start");
                mSocketManager.stopSocketListener(eventName);
                eventNameField.setEditable(true);
                event.setListening(false);
            } else {
                listenerButton.setText("Stop");
                event.setListening(true);
                eventNameField.setEditable(true);
                mSocketManager.startSocketListener(eventName, () -> {
                    Platform.runLater(() -> {
                        int eventNumber = event.getNotifyNumber();
                        eventNumber = eventNumber + 1;
                        event.setNotifyNumber(eventNumber);
                        listenCounterLabel.setText(String.valueOf(eventNumber));
                    });
                });
            }
        });
        setGraphic(eventViewLayout);
    }
}
