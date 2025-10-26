package com.pantrypal.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "pantrypal_prefs";
    private static final String KEY_FIRST_TIME = "first_time";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isFirstTime(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_FIRST_TIME, true);
    }

    public static void setFirstTime(Context context, boolean isFirstTime) {
        getSharedPreferences(context).edit().putBoolean(KEY_FIRST_TIME, isFirstTime).apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_LOGGED_IN, false);
    }

    public static void setLoggedIn(Context context, boolean isLoggedIn) {
        getSharedPreferences(context).edit().putBoolean(KEY_LOGGED_IN, isLoggedIn).apply();
    }

    public static int getUserId(Context context) {
        return getSharedPreferences(context).getInt(KEY_USER_ID, -1);
    }

    public static void setUserId(Context context, int userId) {
        getSharedPreferences(context).edit().putInt(KEY_USER_ID, userId).apply();
    }

    public static String getUserEmail(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_EMAIL, "");
    }

    public static void setUserEmail(Context context, String email) {
        getSharedPreferences(context).edit().putString(KEY_USER_EMAIL, email).apply();
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(KEY_USER_NAME, "");
    }

    public static void setUserName(Context context, String name) {
        getSharedPreferences(context).edit().putString(KEY_USER_NAME, name).apply();
    }

    public static void logout(Context context) {
        getSharedPreferences(context).edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .putInt(KEY_USER_ID, -1)
                .putString(KEY_USER_EMAIL, "")
                .putString(KEY_USER_NAME, "")
                .apply();
    }
}