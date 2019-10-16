package com.tokentm.sdk.common;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.annotation.RequiresApi;

import com.xxf.arch.XXF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SharedPreferenceWrapper {
    private SharedPreferences mSharedPreferences;
    private Editor mEditor;
    private ArrayList<OnSharedPreferenceChangeListener> mListeners;

    public SharedPreferenceWrapper(String fileName) {
        this.mSharedPreferences = XXF.getApplication().getSharedPreferences(fileName, 0);
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }

    }

    public void registerChangeListener(OnSharedPreferenceChangeListener listener) {
        this.mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterChangeListener(OnSharedPreferenceChangeListener listener) {
        this.mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public synchronized boolean registerListener(OnSharedPreferenceChangeListener listener) {
        boolean ret = false;
        if (listener != null) {
            if (!this.mListeners.contains(listener)) {
                ret = this.mListeners.add(listener);
            }

            this.mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
        }

        return ret;
    }

    public synchronized boolean unregisterListener(OnSharedPreferenceChangeListener listener) {
        boolean ret = false;
        if (listener != null) {
            if (this.mListeners.contains(listener)) {
                ret = this.mListeners.remove(listener);
            }

            this.mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
        }

        return ret;
    }

    public boolean contains(String key) {
        return this.mSharedPreferences.contains(key);
    }

    public void removeKey(String key) {
        if (this.mEditor == null) {
            this.mEditor = this.mSharedPreferences.edit();
        }

        this.mEditor.remove(key);
        this.mEditor.commit();
    }

    public void clear() {
        if (this.mEditor == null) {
            this.mEditor = this.mSharedPreferences.edit();
        }

        this.mEditor.clear();
        this.mEditor.commit();
    }

    public String getString(String key) {
        return this.mSharedPreferences.getString(key, "");
    }

    public void putString(String key, String value) {
        if (this.mEditor == null) {
            this.mEditor = this.mSharedPreferences.edit();
        }

        this.mEditor.putString(key, value);
        this.mEditor.commit();
    }

    @RequiresApi(
            api = 11
    )
    public Set<String> getStringSet(String key) {
        return this.mSharedPreferences.getStringSet(key, new HashSet());
    }

    @RequiresApi(
            api = 11
    )
    public void putStringSet(String key, Set<String> values) {
        if (this.mEditor == null) {
            this.mEditor = this.mSharedPreferences.edit();
        }

        this.mEditor.putStringSet(key, values);
        this.mEditor.commit();
    }

    public int getInt(String key) {
        return this.mSharedPreferences.getInt(key, -1);
    }

    public void putInt(String key, int value) {
        if (this.mEditor == null) {
            this.mEditor = this.mSharedPreferences.edit();
        }

        this.mEditor.putInt(key, value);
        this.mEditor.commit();
    }

    public long getLong(String key) {
        return this.mSharedPreferences.getLong(key, -1L);
    }

    public void putLong(String key, long value) {
        if (this.mEditor == null) {
            this.mEditor = this.mSharedPreferences.edit();
        }

        this.mEditor.putLong(key, value);
        this.mEditor.commit();
    }

    public float getFloat(String key) {
        return this.mSharedPreferences.getFloat(key, 0.0F);
    }

    public void putFloat(String key, float value) {
        if (this.mEditor == null) {
            this.mEditor = this.mSharedPreferences.edit();
        }

        this.mEditor.putFloat(key, value);
        this.mEditor.commit();
    }

    public boolean getBoolean(String key) {
        return this.mSharedPreferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        if (this.mEditor == null) {
            this.mEditor = this.mSharedPreferences.edit();
        }

        this.mEditor.putBoolean(key, value);
        this.mEditor.commit();
    }

    public Map<String, ?> getAll() {
        return this.mSharedPreferences.getAll();
    }
}
