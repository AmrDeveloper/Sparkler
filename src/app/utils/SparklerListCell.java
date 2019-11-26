package app.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public abstract class SparklerListCell<T> extends ListCell<T> {

    private String mViewPath;
    private FXMLLoader mViewLoader;

    public abstract void onViewUpdate(T item);

    public SparklerListCell(String viewPath) {
        this.mViewPath = viewPath;
    }

    private void loadItemView() {
        mViewLoader = new FXMLLoader(getClass().getResource(mViewPath));
        mViewLoader.setController(this);
        try {
            mViewLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (mViewLoader == null) {
                loadItemView();
            }
            onViewUpdate(item);
        }
    }
}
