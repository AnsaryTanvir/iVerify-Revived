package com.iverify.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "UserCredentials";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    // Save username and password
    public static void saveCredentials(Context context, String username, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply(); // Save asynchronously
    }

    // Retrieve username and password
    public static String[] getCredentials(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_USERNAME, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);
        return new String[]{username, password};
    }

    // Check if credentials are saved
    public static boolean areCredentialsSaved(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(KEY_USERNAME) && sharedPreferences.contains(KEY_PASSWORD);
    }
}