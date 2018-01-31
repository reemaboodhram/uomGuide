package com.boodhram.guideme.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.boodhram.guideme.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class SharedPreferenceHelper {

    public static void putAccountInSharedPrefence(Context context, AccountDTO accountDTO){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(accountDTO);
        editor.putString(CONSTANTS.ACCOUNT, json);
        editor.commit();

    }

    public static AccountDTO getAccountFromShared(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(CONSTANTS.ACCOUNT, null);
        Type type = new TypeToken<AccountDTO>() {}.getType();
        AccountDTO account = gson.fromJson(json, type);
        return account;

    }

    public static String getChatWithToShared(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(CONSTANTS.CHATWITH, null);
    }

    public static void setChatWithToShared(Context context, String string) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CONSTANTS.CHATWITH, string);
        editor.commit();
    }

    public static void clearall(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();

    }
}
