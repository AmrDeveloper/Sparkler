package app.utils;

import java.util.prefs.Preferences;

public class Settings {

    private Preferences mSettingsPreference;

    private static final String SETTINGS = "settings";

    private static final String TIMEOUT = "timeout";
    private static final int DEFAULT_TIMEOUT = 10_000;

    private static final String THEME = "theme";
    private static final String DEFAULT_THEME = "WHITE";

    private OnSettingChange mOnSettingChange;

    public Settings() {
        mSettingsPreference = Preferences.userRoot().node(SETTINGS);
        mSettingsPreference.addPreferenceChangeListener(event -> {
            if(mOnSettingChange == null){
                return;
            }
            String key = event.getKey();
            switch (key){
                case TIMEOUT :
                    mOnSettingChange.onTimeoutChange(Integer.parseInt(event.getNewValue()));
                    break;
                case THEME :
                    mOnSettingChange.onThemeChange(event.getNewValue());
                    break;
            }
        });
    }

    public Preferences getSettingPreference(){
        return mSettingsPreference;
    }

    public void setOnSettingChange(OnSettingChange listener){
        mOnSettingChange = listener;
    }

    public void setTimeout(int timeout) {
        mSettingsPreference.putInt(TIMEOUT, timeout);
    }

    public int getTimeout() {
        return mSettingsPreference.getInt(TIMEOUT, DEFAULT_TIMEOUT);
    }

    public void setTheme(String theme) {
        mSettingsPreference.put(THEME, theme);
    }

    public String getTheme() {
        return mSettingsPreference.get(THEME, DEFAULT_THEME);
    }
}
