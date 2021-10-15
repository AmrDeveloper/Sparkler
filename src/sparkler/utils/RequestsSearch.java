package sparkler.utils;

import sparkler.model.Request;
import javafx.beans.value.ChangeListener;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class RequestsSearch {

    private TextField mSearchTextField;
    private ListView<Request> mRequestsListView;

    public RequestsSearch(TextField textField, ListView<Request> listView) {
        mSearchTextField = textField;
        mRequestsListView = listView;
    }

    public void bindSearch() {
        mSearchTextField.textProperty().addListener(mRequestListFilter);
    }

    public void unBindSearch() {
        mSearchTextField.textProperty().removeListener(mRequestListFilter);
    }

    private final ChangeListener<String> mRequestListFilter = (observable, oldValue, newValue) -> {
        //TODO : Filter list view
        if(newValue.isEmpty()){

            return;
        }

    };
}
