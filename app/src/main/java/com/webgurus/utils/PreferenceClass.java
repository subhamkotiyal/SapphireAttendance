package com.webgurus.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;


/*
PreferenceClass.setBooleanPreference(mContext, Constants.IS_LOGIN, false);
PreferenceClass.setStringPreference(mContext, Constants.REGISTER_API_TOKEN,"");
PreferenceClass.getStringPreferences(mContext,Constants.LOGIN_API_TOKEN);
 */
/*
        //save login user info in the app
        PreferenceClass.putObjectPreference(mContext,Constants.LOGIN_USER_INFO, patientRecordPojo);
        //get saved login user object from shared preference
        PatientRecordPojo patientRecordPojo =
        PreferenceClass.getObjectPreference(mContext,Constants.LOGIN_USER_INFO,PatientRecordPojo.class);
*/

// http://blog.nkdroidsolutions.com/class-object-in-sharedpreferences/
public class PreferenceClass {
    public static Context appContext;
    private static String PREFERENCE_CONSTANT = "dr";
    private static Gson GSON = new Gson();

    public static void setStringPreference(Context context, String name, String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static void putObjectPreference(Context context, String key, Object object) {

        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        SharedPreferences.Editor editor = settings.edit();

        /*if (object == null) {
            throw new IllegalArgumentException("object is null");
        }*/

        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("key is empty or null");
        }

        editor.putString(key, GSON.toJson(object));
        editor.commit();
    }


    public static void setIntegerPreference(Context context, String name, int value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static void setBooleanPreference(Context context, String name, boolean value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static String getStringPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        return settings.getString(name, "");
    }

    public static <T> T getObjectPreference(Context context, String key, Class<T> a) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        String gson = preferences.getString(key, null);
        if (gson == null) {
            return null;
        } else {
            try {
                return GSON.fromJson(gson, a);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
            }
        }
    }

    public static void setTimePreference(Context context, String name, Long value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(name, value);
        editor.commit();
    }

    public static Long getDatePreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        return settings.getLong(name, 0);
    }

    public static int getIntegerPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        return settings.getInt(name, 0);
    }

    public static boolean getBooleanPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        return settings.getBoolean(name, false);
    }

    public static void clearPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_CONSTANT, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
