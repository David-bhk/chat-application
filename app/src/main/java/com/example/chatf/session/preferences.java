package com.example.chatf.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class preferences {
    private static final String KEY_DATA = "key_data";
    private static final String NAME_DATA = "name_data";
    private static final String ACTIVE_DATA = "active_data";
    private static final SharedPreferences getSharedPreferences (Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static void setKeyData(Context context, String s){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_DATA, s);
        editor.apply();
    }
    public static String getKeyData(Context context){
        return getSharedPreferences(context).getString(KEY_DATA,"");

    }

    public static void setActiveData(Context context, boolean isActive) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(ACTIVE_DATA, isActive);
        editor.apply();

    }
    public static boolean getActiveData(Context context){
        return getSharedPreferences(context).getBoolean(ACTIVE_DATA, false);
    }

    public static void setNameData(Context context, String s){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(NAME_DATA, s);
        editor.apply();
    }
    public static String getNameData(Context context){
        return getSharedPreferences(context).getString(NAME_DATA,"");
    }
    private static void clearData (Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_DATA);
        editor.remove(NAME_DATA);
        editor.apply();
    }

}
