package com.royalenfield.reprime.preference;

import android.content.Context;

import java.util.Map;
import java.util.Set;

/**
 * @author BOP1KOR on 2/8/2019.
 */

public interface IPreferenceHelper {
    Map<String, ?> getAll(Context context) throws PreferenceException;

    String getString(Context context, String key) throws PreferenceException;

    Set<String> getStringSet(Context context, String key) throws PreferenceException;

    int getInt(Context context, String key) throws PreferenceException;

    long getLong(Context context, String key) throws PreferenceException;

    float getFloat(Context context, String key) throws PreferenceException;

    boolean getBoolean(Context context, String key) throws PreferenceException;

    boolean contains(Context context, String key) throws PreferenceException;

    void putString(Context context, String key, String value) throws PreferenceException;

    void putStringSet(Context context, String key, Set<String> values) throws PreferenceException;

    void putInt(Context context, String key, int value) throws PreferenceException;

    void putLong(Context context, String key, long value) throws PreferenceException;

    void putFloat(Context context, String key, float value) throws PreferenceException;

    void putBoolean(Context context, String key, boolean value) throws PreferenceException;

    void remove(Context context, String key) throws PreferenceException;

    void removeAll(Context context) throws PreferenceException;

    String getString(Context context, String key,String defaultValue) throws PreferenceException;
}
