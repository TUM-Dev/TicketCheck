package de.tum.in.tca.ticketcheck.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import de.tum.in.tca.ticketcheck.BuildConfig;

/**
 * Class for common helper functions used by a lot of classes.
 */
public final class Utils {
    private static final String LOGGING_REGEX = "[a-zA-Z0-9.]+\\.";

    private Utils() {
        // Utils is a utility class
    }

    /**
     * Get a value from the default shared preferences
     *
     * @param c          Context
     * @param key        setting name
     * @param defaultVal default value
     * @return setting value, defaultVal if undefined
     */
    public static String getSetting(Context c, String key, String defaultVal) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        return sp.getString(key, defaultVal);
    }

    /**
     * Get a value from the default shared preferences.
     *
     * @param c          Context
     * @param key        setting name
     * @param defaultVal default value
     * @return setting value, defaultVal if undefined
     */
    public static Long getSettingLong(Context c, String key, Long defaultVal) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sp.getLong(key, defaultVal);
        } catch (ClassCastException ignore) {
            try {
                return Long.valueOf(sp.getString(key, null));
            } catch (NumberFormatException ignore2) {
                return defaultVal;
            }
        }
    }

    /**
     * Get a value from the default shared preferences.
     *
     * @param c          Context
     * @param key        setting name
     * @param defaultVal default value
     * @return setting value, defaultVal if undefined
     */
    public static Float getSettingFloat(Context c, String key, Float defaultVal) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sp.getFloat(key, defaultVal);
        } catch (ClassCastException ignore) {
            try {
                return Float.valueOf(sp.getString(key, null));
            } catch (NumberFormatException ignore2) {
                return defaultVal;
            }
        }
    }

    /**
     * Get a value from the default shared preferences.
     *
     * @param c          Context
     * @param key        setting name
     * @param defaultVal default value
     * @return setting value, defaultVal if undefined
     */
    public static Integer getSettingInt(Context c, String key, Integer defaultVal) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            return sp.getInt(key, defaultVal);
        } catch (ClassCastException ignore) {
            try {
                return Integer.valueOf(sp.getString(key, null));
            } catch (NumberFormatException ignore2) {
                return defaultVal;
            }
        }
    }

    /**
     * Get a value from the default shared preferences.
     *
     * @param c         Context
     * @param key       setting name
     * @param classInst e.g. ChatMember.class
     * @return setting value, "" if undefined
     */
    public static <T> T getSetting(Context c, String key, Class<T> classInst) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        String val = sp.getString(key, null);
        if (val != null) {
            return new Gson().fromJson(val, classInst);
        }
        return null;
    }

    /**
     * Return the boolean value of a setting.
     *
     * @param c          Context
     * @param name       setting name
     * @param defaultVal default value
     * @return true if setting was checked, else value
     */
    public static boolean getSettingBool(Context c, String name, boolean defaultVal) {
        return PreferenceManager.getDefaultSharedPreferences(c)
                                .getBoolean(name, defaultVal);
    }

    /**
     * Logs an exception and additional information
     * Use this anywhere in the app when a fatal error occurred.
     * If you can give a better description of what went wrong
     * use {@link #log(Throwable, String)} instead.
     *
     * @param e Exception (source for message and stack trace)
     */
    public static void log(Throwable e) {
        try (StringWriter sw = new StringWriter()) {
            e.printStackTrace(new PrintWriter(sw));
            String s = Thread.currentThread()
                             .getStackTrace()[3].getClassName()
                                                .replaceAll(LOGGING_REGEX, "");
            Log.e(s, e + "\n" + sw);
        } catch (IOException e1) {
            // there is a time to stop logging errors
        }
    }

    /**
     * Logs an exception and additional information
     * Use this anywhere in the app when a fatal error occurred.
     * If you can't give an exact error description simply use
     * {@link #log(Throwable)} instead.
     *
     * @param e       Exception (source for message and stack trace)
     * @param message Additional information for exception message
     */
    public static void log(Throwable e, String message) {
        try (StringWriter sw = new StringWriter()) {
            e.printStackTrace(new PrintWriter(sw));
            String s = Thread.currentThread()
                             .getStackTrace()[3].getClassName()
                                                .replaceAll(LOGGING_REGEX, "");
            Log.e(s, e + " " + message + '\n' + sw);
        } catch (IOException e1) {
            // there is a time to stop logging errors
        }
    }

    /**
     * Logs a message
     * Use this to log the current app state.
     *
     * @param message Information or Debug message
     */
    public static void log(String message) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        String s = Thread.currentThread()
                         .getStackTrace()[3].getClassName()
                                            .replaceAll(LOGGING_REGEX, "");
        Log.d(s, message);
    }

    /**
     * Sets the value of a setting
     *
     * @param c   Context
     * @param key setting key
     */
    public static void setSetting(Context c, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        sp.edit()
          .putBoolean(key, value)
          .apply();
    }

    /**
     * Sets the value of a setting
     *
     * @param c     Context
     * @param key   setting key
     * @param value String value
     */
    public static void setSetting(Context c, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        sp.edit()
          .putString(key, value)
          .apply();
    }

    /**
     * Sets the value of a setting
     *
     * @param c   Context
     * @param key setting key
     */
    public static void setSetting(Context c, String key, Object value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        sp.edit()
          .putString(key, new Gson().toJson(value))
          .apply();
    }

    /**
     * Shows a long {@link Toast} message.
     *
     * @param context The activity where the toast is shown
     * @param msg     The toast message id
     */
    public static void showToast(Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG)
             .show();
    }

    /**
     * Shows a long {@link Toast} message.
     *
     * @param context The activity where the toast is shown
     * @param msg     The toast message
     */
    public static void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG)
             .show();
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            return context.getPackageManager()
                          .getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
        }
        return 0;
    }
}
