package app.model;

import app.utils.SparklerListCell;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class EventListCell extends SparklerListCell<Event> {

    @FXML private AnchorPane eventViewLayout;

    @FXML private Label listenCounterLabel;
    @FXML private TextField eventNameField;
    @FXML private Button listenerButton;

    private static final String EVENT_VIEW_LAYOUT = "views/event_view.fxml";

    public EventListCell() {
        super(EVENT_VIEW_LAYOUT);
    }

    @Override
    public void onViewUpdate(Event event) {
        listenCounterLabel.setText(event.getNotifyNumber() + "");

        listenerButton.setOnMouseClicked(e -> {
            eventNameField.setEditable(false);
            int listenCount = event.getNotifyNumber() + 1;
            event.setNotifyNumber(listenCount);
            listenCounterLabel.setText(String.valueOf(listenCount));
        });
        setGraphic(eventViewLayout);
    }
}
