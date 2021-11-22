package sparkler.utils;

import sparkler.model.Attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Environment {

    private final Preferences preferences;
    public static final String GLOBALS = "globals";
    public static final String GLOBALS_ACTIVE = "globals_active";

    public Environment() {
        this.preferences = Preferences.userNodeForPackage(Environment.class);
    }

    public Preferences getEnvironmentByName(String name) {
        return preferences.node(name);
    }

    public String[] getEnvironments() {
       try {
           return preferences.childrenNames();
       } catch (BackingStoreException e) {
           return new String[]{};
       }
    }

    public List<Attribute> loadEnvironmentAttributes() {
        List<Attribute> attributes = new ArrayList<>();
        Preferences globals = getEnvironmentByName(GLOBALS);
        Preferences globalsActive = getEnvironmentByName(GLOBALS_ACTIVE);
        String[] keys;
        try {
            keys = globals.keys();
        } catch (BackingStoreException e) {
            keys = new String[]{};
        }

        for(String key : keys) {
            Attribute attribute = new Attribute(key, globals.get(key, key));
            if (!globalsActive.get(key, "").isEmpty()) {
                attribute.setUserChoice(true);
            }
            attributes.add(attribute);
        }

        return attributes;
    }

    public void updateEnvironmentAttributes(List<Attribute> attributes) {
        Preferences globals = getEnvironmentByName(GLOBALS);
        Preferences globalsActive = getEnvironmentByName(GLOBALS_ACTIVE);

        try {
            globals.clear();
            globalsActive.clear();
        } catch (BackingStoreException ignored) {}


        for(Attribute attribute : attributes) {
            globals.put(attribute.getKey(), attribute.getValue());
            if (attribute.isUserChoice()) {
                globalsActive.put(attribute.getKey(), attribute.getValue());
            }
        }
    }

    public String getActiveVariableValue(String variable) {
        Preferences preferences = getEnvironmentByName(GLOBALS_ACTIVE);
        return preferences.get(variable, "");
    }
}
