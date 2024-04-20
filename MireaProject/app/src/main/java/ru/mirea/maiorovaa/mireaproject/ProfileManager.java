package ru.mirea.maiorovaa.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfileManager {

    private static final String PREF_NAME = "ProfilePrefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";

    private SharedPreferences preferences;

    public ProfileManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveProfile(String name, int age) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putInt(KEY_AGE, age);
        editor.apply();
    }

    public String getName() {
        return preferences.getString(KEY_NAME, "");
    }

    public int getAge() {
        return preferences.getInt(KEY_AGE, 0);
    }
}
