package app.model;

public class Event {

    private String name;
    private int notifyNumber;
    private boolean isListening;

    public Event(String name, int notifyNumber, boolean isListening) {
        this.name = name;
        this.notifyNumber = notifyNumber;
        this.isListening = isListening;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNotifyNumber() {
        return notifyNumber;
    }

    public void setNotifyNumber(int notifyNumber) {
        this.notifyNumber = notifyNumber;
    }

    public boolean isListening() {
        return isListening;
    }

    public void setListening(boolean listening) {
        isListening = listening;
    }

    @Override
    public String toString() {
        return name;
    }
}
