package com.baculsoft.gcm.simple.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Budi Oktaviyan Suryanto (budioktaviyans@gmail.com)
 */
public class Preferences {

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    private static boolean getBoolean(SharedPreferences sharedPreferences, String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    private static String getString(SharedPreferences sharedPreferences, String key) {
        return sharedPreferences.getString(key, null);
    }

    /**
     * Get GCM Token
     *
     * @param context sharedPreferences
     * @return String GCM_TOKEN
     */
    public static String getGcmToken(Context context) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return getString(sharedPreferences, IConstants.IPreferences.GCM_TOKEN);
    }

    /**
     * Get GCM Token Status
     *
     * @param context sharedPreferences
     * @return boolean GCM_TOKEN_STATUS
     */
    public static boolean getGcmTokenStatus(Context context) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return getBoolean(sharedPreferences, IConstants.IPreferences.GCM_TOKEN_STATUS, false);
    }

    /**
     * Set GCM Token
     *
     * @param context        sharedPreferences
     * @param gcmToken       GCM_TOKEN
     * @param gcmTokenStatus GCM_TOKEN_STATUS
     */
    public static void setGcmToken(Context context, String gcmToken, boolean gcmTokenStatus) {
        SavePreferenceBuilder.edit(context).put(IConstants.IPreferences.GCM_TOKEN, gcmToken)
                                           .putBoolean(IConstants.IPreferences.GCM_TOKEN_STATUS, gcmTokenStatus)
                                           .commit();
    }

    private static class SavePreferenceBuilder {
        private SharedPreferences mSharedPreferences;
        private SharedPreferences.Editor mEditor;

        private SavePreferenceBuilder(Context context) {
            mSharedPreferences = Preferences.getSharedPreference(context);
            mEditor = mSharedPreferences.edit();
            mEditor.apply();
        }

        private static SavePreferenceBuilder edit(Context context) {
            return new SavePreferenceBuilder(context);
        }

        public SavePreferenceBuilder putBoolean(String key, boolean value) {
            mEditor.putBoolean(key, value);
            return this;
        }

        public SavePreferenceBuilder put(String key, String value) {
            mEditor.putString(key, value);
            return this;
        }

        public boolean commit() {
            return mEditor.commit();
        }
    }
}