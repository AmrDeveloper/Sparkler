package sparkler.utils;

import java.util.prefs.Preferences;

public class Settings {

    private Preferences mSettingsPreference;

    private static final String SETTINGS = "settings";

    private static final String CONNECT_TIMEOUT = "connect_timeout";
    private static final String READ_TIMEOUT = "read_timeout";
    private static final String WRITE_TIMEOUT = "write_timeout";

    private static final int DEFAULT_CONNECT_TIMEOUT = 10_000;
    private static final int DEFAULT_READ_TIMEOUT = 10_000;
    private static final int DEFAULT_WRITE_TIMEOUT = 30_000;

    private static final String THEME = "theme";
    private static final Theme DEFAULT_THEME = Theme.WHITE;

    private OnThemeChangeListener mOnThemeChangeListener;
    private OnTimeoutChangeListener mOnTimeoutChangeListener;

    public Settings() {
        mSettingsPreference = Preferences.userRoot().node(SETTINGS);
        mSettingsPreference.addPreferenceChangeListener(event -> {
            if(event.getKey().equals(THEME)){
                if(mOnThemeChangeListener == null){
                    return;
                }
                mOnThemeChangeListener.onThemeChange(Theme.valueOf(event.getNewValue()));
            }else{
                if(mOnTimeoutChangeListener == null){
                    return;
                }
                switch (event.getKey()){
                    case CONNECT_TIMEOUT:
                        mOnTimeoutChangeListener.onConnectTimeChange(Integer.parseInt(event.getNewValue()));
                        break;
                    case READ_TIMEOUT:
                        mOnTimeoutChangeListener.onReadTimeChange(Integer.parseInt(event.getNewValue()));
                        break;
                    case WRITE_TIMEOUT:
                        mOnTimeoutChangeListener.onWriteTimeChange(Integer.parseInt(event.getNewValue()));
                        break;
                }
            }
        });
    }

    public Preferences getSettingPreference(){
        return mSettingsPreference;
    }

    public void setThemeChangeListener(OnThemeChangeListener listener){
        mOnThemeChangeListener = listener;
    }

    public void setOnTimeoutChange(OnTimeoutChangeListener listener){
        mOnTimeoutChangeListener = listener;
    }

    public void setConnectTimeout(int timeout) {
        mSettingsPreference.putInt(CONNECT_TIMEOUT, timeout);
    }

    public int getConnectTimeout() {
        return mSettingsPreference.getInt(CONNECT_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
    }

    public void setReadTimeout(int timeout) {
        mSettingsPreference.putInt(READ_TIMEOUT, timeout);
    }

    public int getReadTimeout() {
        return mSettingsPreference.getInt(READ_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    public void setWriteTimeout(int timeout) {
        mSettingsPreference.putInt(WRITE_TIMEOUT, timeout);
    }

    public int getWriteTimeout() {
        return mSettingsPreference.getInt(WRITE_TIMEOUT, DEFAULT_WRITE_TIMEOUT);
    }

    public void setTheme(Theme theme) {
        mSettingsPreference.put(THEME, theme.name());
    }

    public String getTheme() {
        return mSettingsPreference.get(THEME, DEFAULT_THEME.name());
    }
}
