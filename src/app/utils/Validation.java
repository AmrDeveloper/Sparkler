package app.utils;

public class Validation {

    public static boolean isValidNumber(String text){
        if (text.matches("[0-9]+")) {
            return true;
        }
        return false;
    }
}
