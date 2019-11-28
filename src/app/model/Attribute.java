package app.model;

public class Attribute {

    private String key;
    private String value;
    private boolean isUserChoice;

    public Attribute(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUserChoice(boolean userChoice) {
        this.isUserChoice = userChoice;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isUserChoice() {
        return isUserChoice;
    }

    @Override
    public String toString() {
        return value;
    }
}
