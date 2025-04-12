package com.livescore.soccerscore.matchlive.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SPUtils {
    public static final String SHARED_PREFS_NAME = "Live Soccer Scores: Live Match";
    public static String LIST_RECENT = "LIST_RECENT";
    public static String INTENT_TEAM = "INTENT_TEAM";
    public static String INTENT_FIXTURE = "INTENT_FIXTURE";
    public static String INTENT_LEAGUE = "INTENT_LEAGUE";
    public static String NOTIFICATION = "NOTIFICATION";
    public static String LANGUAGE = "LANGUAGE";
    public static String RATE_STAR = "RATE_STAR";
    static Gson gson = new Gson();


    public static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void setString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    public static String getString(Context context, String str, String str2) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getString(str, str2);
    }
    public static void setLong(Context context, String str, long i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putLong(str, i);
        edit.apply();
    }

    public static long getLong(Context context, String str, long i) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getLong(str, i);
    }

    public static void setInt(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public static int getInt(Context context, String str, int i) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getInt(str, i);
    }

    public static void setFloat(Context context, String str, float i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putFloat(str, i);
        edit.apply();
    }

    public static float getFloat(Context context, String str, float i) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getFloat(str, i);
    }

    public static void setBoolean(Context context, String str, boolean b) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        edit.putBoolean(str, b);
        edit.apply();
    }

    public static boolean getBoolean(Context context, String str, boolean b) {
        return context.getSharedPreferences(SHARED_PREFS_NAME, 0).getBoolean(str, b);
    }

    public static void setList(Context context, String KEY_LIST, ArrayList<String> list) {
        String json = gson.toJson(list); // Chuyển ArrayList thành JSON
        context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().putString(KEY_LIST, json).apply();
    }

    // Lấy ArrayList từ SharedPreferences
    public static ArrayList<String> getList(Context context, String KEY_LIST) {
        String json = context.getSharedPreferences(SHARED_PREFS_NAME, 0).getString(KEY_LIST, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    // Xóa ArrayList khỏi SharedPreferences
    public static void removeList(Context context, String KEY_LIST) {
        context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().remove(KEY_LIST).apply();
    }
    public static void showKeyboard(Context context, View view) {
        if (view == null) return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();

        view.post(() -> {
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    public static void hideKeyboard(Context context, View view) {
        if (view == null) return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
