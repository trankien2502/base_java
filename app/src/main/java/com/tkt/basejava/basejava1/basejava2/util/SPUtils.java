package com.tkt.basejava.basejava1.basejava2.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkt.basejava.basejava1.basejava2.R;
import com.tkt.basejava.basejava1.basejava2.item.ItemFunctionIcon;

import java.util.ArrayList;
import java.lang.reflect.Type;

public class SPUtils {
    public static Gson gson = new Gson();
    public static final String SHARED_PREFS_NAME = "Base Java TKT";
    public static String CAMERA = "CAMERA";
    public static String NOTIFICATION = "NOTIFICATION";
    public static String LANGUAGE = "LANGUAGE";
    public static String RATE_STAR = "RATE_STAR";

    public static String INTENT_SELECT_FUNCTION = "INTENT_SELECT_FUNCTION";
    public static String FLOATING_ICON_SINGLE_TAP = "FLOATING_ICON_SINGLE_TAP";
    public static String FLOATING_ICON_DOUBLE_TAP = "FLOATING_ICON_DOUBLE_TAP";
    public static String FLOATING_ICON_LONG_PRESS = "FLOATING_ICON_LONG_PRESS";
    public static String MENU_FUNCTION_1 = "MENU_FUNCTION_1";
    public static String MENU_POSITION = "MENU_POSITION";
    public static String MENU_FUNCTION_2 = "MENU_FUNCTION_2";


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

    public static void setObject(Context context, String KEY_OBJECT, ItemFunctionIcon object) {
        SharedPreferences.Editor edit = context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit();
        String json = gson.toJson(object); // Chuyển Object thành JSON
        edit.putString(KEY_OBJECT, json).apply();
    }

    public static ItemFunctionIcon getObject(Context context, String KEY_OBJECT, ItemFunctionIcon defaultItem) {
        String json = context.getSharedPreferences(SHARED_PREFS_NAME, 0).getString(KEY_OBJECT, null);
        return json != null ? gson.fromJson(json, ItemFunctionIcon.class) : defaultItem;//new ItemFunctionIcon(R.drawable.ic_function_none,R.string.none)
    }

    // Xóa Object khỏi SharedPreferences
    public static void removeObject(Context context, String KEY_OBJECT) {
        context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().remove(KEY_OBJECT).apply();
    }

    // Lưu ArrayList vào SharedPreferences
    public static void setList(Context context, String KEY_LIST, ArrayList<ItemFunctionIcon> list) {
        String json = gson.toJson(list); // Chuyển ArrayList thành JSON
        context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().putString(KEY_LIST, json).apply();
    }

    // Lấy ArrayList từ SharedPreferences
    public static ArrayList<ItemFunctionIcon> getList(Context context, String KEY_LIST, ArrayList<ItemFunctionIcon> defaultList) {
        String json = context.getSharedPreferences(SHARED_PREFS_NAME, 0).getString(KEY_LIST, null);
        if (json == null) return defaultList; // Trả về danh sách rỗng nếu không có dữ liệu
        Type type = new TypeToken<ArrayList<ItemFunctionIcon>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    // Xóa ArrayList khỏi SharedPreferences
    public static void removeList(Context context, String KEY_LIST) {
        context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().remove(KEY_LIST).apply();
    }
}
