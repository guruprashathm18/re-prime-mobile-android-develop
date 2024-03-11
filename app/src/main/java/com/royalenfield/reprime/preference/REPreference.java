package com.royalenfield.reprime.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.ui.home.service.sharedpreference.REServiceSharedPreference;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.royalenfield.reprime.utils.RELog;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author BOP1KOR on 2/8/2019.
 */

public class REPreference implements IPreferenceHelper {
    private static final String TAG = REPreference.class.getSimpleName();

    //Default values.
    public static final String DEFAULT_STRING = "";
    private static final int DEFAULT_INTEGER = -1;
    private static final long DEFAULT_LONG = -1L;
    private static final float DEFAULT_FLOAT = -1F;
    private static final boolean DEFAULT_BOOL = false;
    private static final Set<String> DEFAULT_STRING_SET = Collections.emptySet();
    //File name.
    private static final String PREFS_FILE_NAME = "PREFS_RE";
    //Singleton instance.
    private static REPreference mInstance;

    //Default constructor.
    private REPreference() {
    }

    /**
     * Getter to retrieve the singleton instance.
     *
     * @return {@link REPreference} instance.
     */
    public static synchronized  REPreference getInstance() {
        if (mInstance == null)
            mInstance = new REPreference();
        return mInstance;
    }

    /**
     * Returns the {@link SharedPreferences} object.
     *
     * @param context The context.
     * @param mode    The type of mode. One of the values in {@link Context#MODE_PRIVATE} or {@link Context#MODE_APPEND}.
     * @return {@link SharedPreferences} for this application.
     */
    public SharedPreferences getPreferences(Context context, int mode) {
        return context.getSharedPreferences(PREFS_FILE_NAME, mode);
    }

    @Override
    public Map<String, ?> getAll(Context context) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getAll();
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public String getString(Context context, String key) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getString(key, DEFAULT_STRING);
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public Set<String> getStringSet(Context context, String key) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getStringSet(key, DEFAULT_STRING_SET);
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public int getInt(Context context, String key) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getInt(key, DEFAULT_INTEGER);
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public long getLong(Context context, String key) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getLong(key, DEFAULT_LONG);
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public float getFloat(Context context, String key) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getFloat(key, DEFAULT_FLOAT);
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public boolean getBoolean(Context context, String key) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getBoolean(key, DEFAULT_BOOL);
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public boolean contains(Context context, String key) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            return preferences.contains(key);
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public void putString(Context context, String key, String value) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putString(key, value);
            } else {
                throw new PreferenceException(PreferenceException.PREFERENCE_EDIT_PROHIBITED);
            }
            editor.apply();
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public void putStringSet(Context context, String key, Set<String> values) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putStringSet(key, values);
            } else {
                throw new PreferenceException(PreferenceException.PREFERENCE_EDIT_PROHIBITED);
            }
            editor.apply();
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public void putInt(Context context, String key, int value) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putInt(key, value);
            } else {
                throw new PreferenceException(PreferenceException.PREFERENCE_EDIT_PROHIBITED);
            }
            editor.apply();
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public void putLong(Context context, String key, long value) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putLong(key, value);
            } else {
                throw new PreferenceException(PreferenceException.PREFERENCE_EDIT_PROHIBITED);
            }
            editor.apply();
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public void putFloat(Context context, String key, float value) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putFloat(key, value);
            } else {
                throw new PreferenceException(PreferenceException.PREFERENCE_EDIT_PROHIBITED);
            }
            editor.apply();
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public void putBoolean(Context context, String key, boolean value) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putBoolean(key, value);
            } else {
                throw new PreferenceException(PreferenceException.PREFERENCE_EDIT_PROHIBITED);
            }
            editor.apply();
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public void remove(Context context, String key) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.remove(key);
            } else {
                throw new PreferenceException(PreferenceException.PREFERENCE_EDIT_PROHIBITED);
            }
            editor.apply();
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

    @Override
    public void removeAll(Context context) throws PreferenceException {
        REApplication.getInstance().JWTToken=null;
        REServiceSharedPreference.clearBookingInfoPreferences(REApplication.getAppContext());
        try {
            REApplication.getInstance().getEncryptedSharedPreference(context).edit().clear().apply();
        }
        catch (Exception e){
            RELog.e(e);
        }
        Log.d(TAG, "RE Preference cleared");
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public String getString(Context context, String key,String defaultValue) throws PreferenceException {
        SharedPreferences preferences = getPreferences(context, MODE_PRIVATE);
        if (preferences != null) {
            return preferences.getString(key, defaultValue);
        } else {
            throw new PreferenceException(PreferenceException.PREFERENCE_NOT_FOUND);
        }
    }

}
