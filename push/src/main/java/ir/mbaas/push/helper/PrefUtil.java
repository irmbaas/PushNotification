package ir.mbaas.push.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
    public static String SESSION_TOKEN    = "session_token";
    public static String REGISTRATION_ID  = "registration_id";
    public static String GEO_LOCATIONS    = "geo_locations";
    public static String APP_USE_COUNT    = "app_use_count";

    static public final class Prefs {
        public static SharedPreferences get(Context context) {
            return context.getSharedPreferences("_mbaas_pref", 0);
        }
    }

    static public String getString(Context context, String key) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getString(key, "");
    }

    static public String getString(Context context, String key, String defaultString) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getString(key, defaultString);
    }

    static public synchronized void putString(Context context, String key, String value) {
        SharedPreferences settings = Prefs.get(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    static public boolean getBoolean(Context context, String key) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getBoolean(key, false);
    }

    static public boolean getBoolean(Context context, String key, boolean defaultVal) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getBoolean(key, defaultVal);
    }

    static public synchronized void putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = Prefs.get(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    static public int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }

    static public int getInt(Context context, String key, int defaultVal) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getInt(key, defaultVal);
    }

    static public synchronized void putInt(Context context, String key, int value) {
        SharedPreferences settings = Prefs.get(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}