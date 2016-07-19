package ir.mbaas.sdk.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
    public static String LAST_PUSH_RECEIVED    = "last_push_received";
    public static String REGISTRATION_ID  = "registration_id";
    public static String LAST_LOCATION    = "last_location";
    public static String GEO_LOCATIONS    = "geo_locations";
    public static String APP_USE_COUNT    = "app_use_count";
    public static String UPDATE_REFERENCE_ID = "update_reference_id";
    public static String NEXT_UPDATE_TIME = "next_update_time";
    public static String DEVICE_UNIQUE_ID = "device_unique_id";
    public static String UNDELIVERED_PUSHES = "undelivered_pushes";

    static public final class Prefs {
        public static SharedPreferences get(Context context) {
            return context.getSharedPreferences("_mbaas_pref", 0);
        }
    }

    static public String getString(Context context, String key) {
        return getString(context, key, "");
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

    static public long getLong(Context context, String key, long defaultVal) {
        SharedPreferences settings = Prefs.get(context);
        return settings.getLong(key, defaultVal);
    }

    static public synchronized void putLong(Context context, String key, long value) {
        SharedPreferences settings = Prefs.get(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.apply();
    }
}