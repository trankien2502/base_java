package com.assistivetouch.easytouch.homebutton.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.content.ContextCompat;

import com.assistivetouch.easytouch.homebutton.item.app.ItemAppInfo;
import com.assistivetouch.easytouch.homebutton.ui.screenshot.ItemVideoConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.item.control.ItemFunctionIcon;

import java.util.ArrayList;
import java.lang.reflect.Type;

public class SPUtils {
    public static Gson gson = new Gson();
    public static final String SHARED_PREFS_NAME = "Assistive Touch - Home Button";
    public static String CAMERA = "CAMERA";
    public static String NOTIFICATION = "NOTIFICATION";
    public static String LANGUAGE = "LANGUAGE";
    public static String RATE_STAR = "RATE_STAR";

    public static String INTENT_SELECT_FUNCTION = "INTENT_SELECT_FUNCTION";
    public static String INTENT_ALL_APP = "INTENT_ALL_APP";
    public static String FLOATING_ICON_SINGLE_TAP = "FLOATING_ICON_SINGLE_TAP";
    public static String FLOATING_ICON_DOUBLE_TAP = "FLOATING_ICON_DOUBLE_TAP";
    public static String FLOATING_ICON_LONG_PRESS = "FLOATING_ICON_LONG_PRESS";
    public static String ICON_STYLE = "ICON_STYLE";
    public static String MENU_FUNCTION_1 = "MENU_FUNCTION_1";
    public static String MENU_BACKGROUND_COLOR = "MENU_BACKGROUND_COLOR";
    public static String MENU_FUNCTION = "MENU_FUNCTION";
    public static String MENU_POSITION = "MENU_POSITION";
    public static String MENU_FUNCTION_2 = "MENU_FUNCTION_2";
    public static String FAVOURITE_APP = "FAVOURITE_APP";
    public static String FAVOURITE_POSITION = "FAVOURITE_POSITION";
    public static String VOLUME_ON = "VOLUME_ON";
    public static String TOUCH_ON = "TOUCH_ON";


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
    public static void setListFavourite(Context context, String KEY_LIST, ArrayList<ItemAppInfo> list) {
        String json = gson.toJson(list); // Chuyển ArrayList thành JSON
        context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().putString(KEY_LIST, json).apply();
    }

    // Lấy ArrayList từ SharedPreferences
    public static ArrayList<ItemAppInfo> getListFavourite(Context context, String KEY_LIST, ArrayList<ItemAppInfo> defaultList) {
        String json = context.getSharedPreferences(SHARED_PREFS_NAME, 0).getString(KEY_LIST, null);
        if (json == null) return defaultList; // Trả về danh sách rỗng nếu không có dữ liệu
        Type type = new TypeToken<ArrayList<ItemAppInfo>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    // Xóa ArrayList khỏi SharedPreferences
    public static void removeListFavourite(Context context, String KEY_LIST) {
        context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().remove(KEY_LIST).apply();
    }

    public static void vibration(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(100L, -1));
            } else {
                vibrator.vibrate(100L);
            }
        }
    }
    public static int[] getSizes(Context context) {
        return new int[]{context.getSharedPreferences(SHARED_PREFS_NAME, 0).getInt("width_s", 0), context.getSharedPreferences(SHARED_PREFS_NAME, 0).getInt("height_s", 0), context.getSharedPreferences(SHARED_PREFS_NAME, 0).getInt("noti_s", 0)};
    }
    public static void putSize(Context context, int[] iArr) {
        if (iArr[0] != 0) {
            context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().putInt("width_s", iArr[0]).apply();
        }
        if (iArr[1] != 0) {
            context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().putInt("height_s", iArr[1]).apply();
        }
        if (iArr[2] != 0) {
            context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().putInt("noti_s", iArr[2]).apply();
        }
    }
    public static void putRecord(Context context, ItemVideoConfig itemVideoConfig) {
        context.getSharedPreferences(SHARED_PREFS_NAME, 0).edit().putString("record", new Gson().toJson(itemVideoConfig)).apply();
    }

    public static ItemVideoConfig getRecord(Context context) {
        String string = context.getSharedPreferences(SHARED_PREFS_NAME, 0).getString("record", "");
        if (!string.isEmpty()) {
            return (ItemVideoConfig) new Gson().fromJson(string, new TypeToken<ItemVideoConfig>() {
            }.getType());
        }
        return new ItemVideoConfig(false, 1, true, 2500000, 30, 1, 16000, 160000);
    }
    public static Bitmap cropBitmapTransparency(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= bitmap.getWidth()) {
                i2 = 0;
                break;
            } else if (Color.alpha(bitmap.getPixel(i2, bitmap.getHeight() / 2)) > 0) {
                break;
            } else {
                i2++;
            }
        }
        int i3 = 0;
        while (true) {
            if (i3 >= bitmap.getHeight()) {
                break;
            } else if (Color.alpha(bitmap.getPixel(bitmap.getWidth() / 2, i3)) > 0) {
                i = i3;
                break;
            } else {
                i3++;
            }
        }
        int width2 = bitmap.getWidth() - 1;
        while (true) {
            if (width2 < 0) {
                break;
            } else if (Color.alpha(bitmap.getPixel(width2, bitmap.getHeight() / 2)) > 0) {
                width = width2;
                break;
            } else {
                width2--;
            }
        }
        int height2 = bitmap.getHeight() - 1;
        while (true) {
            if (height2 < 0) {
                break;
            } else if (Color.alpha(bitmap.getPixel(bitmap.getWidth() / 2, height2)) > 0) {
                height = height2;
                break;
            } else {
                height2--;
            }
        }
        if (width < i2 || height < i) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, i2, i, width - i2, height - i);
    }
    public static ArrayList<ItemAppInfo> getListDefaultFavourite(Context context) {
        ArrayList<ItemAppInfo> listDefault = new ArrayList<>();
        listDefault.add(new ItemAppInfo(0, null, null));
        listDefault.add(new ItemAppInfo(1, null, null));
        listDefault.add(new ItemAppInfo(2, null, null));
        listDefault.add(new ItemAppInfo(3, null, null));
        listDefault.add(new ItemAppInfo(4, null, null));
        listDefault.add(new ItemAppInfo(5, null, null));
        listDefault.add(new ItemAppInfo(6, null, null));
        listDefault.add(new ItemAppInfo(7, null, null));
        return listDefault;
    }
    public static ArrayList<ItemFunctionIcon> getListDefaultMenu1() {
        ArrayList<ItemFunctionIcon> listDefault = new ArrayList<>();
        listDefault.add(new ItemFunctionIcon(0, ItemFunctionIcon.ACTION_FAVOURITE, R.drawable.ic_function_favourite, R.drawable.ic_action_favourite, R.string.favourite));
        listDefault.add(new ItemFunctionIcon(1, ItemFunctionIcon.ACTION_RECENT, R.drawable.ic_function_recent, R.drawable.ic_action_recent, R.string.recent));
        listDefault.add(new ItemFunctionIcon(2, ItemFunctionIcon.ACTION_NOTIFICATION, R.drawable.ic_function_notification, R.drawable.ic_action_notification, R.string.notification));
        listDefault.add(new ItemFunctionIcon(3, ItemFunctionIcon.ACTION_HOME, R.drawable.ic_function_home, R.drawable.ic_action_home, R.string.home));
        listDefault.add(new ItemFunctionIcon(4, ItemFunctionIcon.ACTION_DEVICE, R.drawable.ic_function_device, R.drawable.ic_action_device, R.string.device));
        listDefault.add(new ItemFunctionIcon(5, ItemFunctionIcon.ACTION_SETTINGS, R.drawable.ic_function_setting, R.drawable.ic_action_setting, R.string.settings));
        return listDefault;
    }

    public static ArrayList<ItemFunctionIcon> getListDefaultMenu2() {
        ArrayList<ItemFunctionIcon> listDefault = new ArrayList<>();
        listDefault.add(new ItemFunctionIcon(0, ItemFunctionIcon.ACTION_WIFI, R.drawable.ic_function_wifi, R.drawable.ic_action_wifi, R.string.wifi));
        listDefault.add(new ItemFunctionIcon(1, ItemFunctionIcon.ACTION_VOLUME_UP, R.drawable.ic_function_volume_cross_up, R.drawable.ic_action_volume_up, R.string.volume_up));
        listDefault.add(new ItemFunctionIcon(2, ItemFunctionIcon.ACTION_LOCK_SCREEN, R.drawable.ic_function_lock_screen, R.drawable.ic_action_lock, R.string.lock_screen));
        listDefault.add(new ItemFunctionIcon(3, ItemFunctionIcon.ACTION_FLASHLIGHT, R.drawable.ic_function_flashlight, R.drawable.ic_action_flashlight, R.string.flashlight));
        listDefault.add(new ItemFunctionIcon(4, ItemFunctionIcon.ACTION_SCREEN_SHOT, R.drawable.ic_function_screen_shoot, R.drawable.ic_action_screen_shoot, R.string.screen_shoot));
        listDefault.add(new ItemFunctionIcon(5, ItemFunctionIcon.ACTION_VOLUME_DOWN, R.drawable.ic_function_volume_cross_down, R.drawable.ic_action_volume_down, R.string.volume_down));
        return listDefault;
    }

    public static ArrayList<ItemFunctionIcon> getListCustomMenu() {
        ArrayList<ItemFunctionIcon> functionIconList = new ArrayList<>();
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_NONE, R.drawable.ic_function_none, R.drawable.ic_action_none, R.string.none));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_BACK, R.drawable.ic_function_back, R.drawable.ic_action_back, R.string.back));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_SCREEN_RECORDER, R.drawable.ic_function_screen_recorder, R.drawable.ic_action_video_recorder, R.string.screen_recorder));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_BLUETOOTH, R.drawable.ic_function_bluetooth, R.drawable.ic_action_bluetooth, R.string.bluetooth));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_AIRPLANE, R.drawable.ic_function_airplane, R.drawable.ic_action_airplane, R.string.airplane));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_LOCATION, R.drawable.ic_function_location, R.drawable.ic_action_location, R.string.location));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_LOCK_ROTATION, R.drawable.ic_function_lock_rotation, R.drawable.ic_action_lock_rotation, R.string.lock_rotation));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_FLASHLIGHT, R.drawable.ic_function_flashlight, R.drawable.ic_action_flashlight, R.string.flashlight));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_VOLUME_OPTION, R.drawable.ic_function_volume_cross, R.drawable.ic_action_volume, R.string.volume_option));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_TIME_OUT, R.drawable.ic_function_time_out, R.drawable.ic_action_time_out, R.string.time_out));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_ALL_APP, R.drawable.ic_function_all_app, R.drawable.ic_action_all_app, R.string.all_app));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_HOME, R.drawable.ic_function_home, R.drawable.ic_action_home, R.string.home));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_WIFI, R.drawable.ic_function_wifi, R.drawable.ic_action_wifi, R.string.wifi));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_BRIGHTNESS, R.drawable.ic_function_bright_ness, R.drawable.ic_action_brightness, R.string.brightness));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_DEVICE, R.drawable.ic_function_device, R.drawable.ic_action_device, R.string.device));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_SCREEN_SHOT, R.drawable.ic_function_screen_shoot, R.drawable.ic_action_screen_shoot, R.string.screen_shoot));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_NOTIFICATION, R.drawable.ic_function_notification, R.drawable.ic_action_notification, R.string.notification));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_FAVOURITE, R.drawable.ic_function_favourite, R.drawable.ic_action_favourite, R.string.favourite));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_RECENT, R.drawable.ic_function_recent, R.drawable.ic_action_recent, R.string.recent));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_LOCK_SCREEN, R.drawable.ic_function_lock_screen, R.drawable.ic_action_lock, R.string.lock_screen));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_POWER, R.drawable.ic_function_power, R.drawable.ic_action_power, R.string.power));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_SETTINGS, R.drawable.ic_function_setting, R.drawable.ic_action_setting, R.string.settings));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_VOLUME_UP, R.drawable.ic_function_volume_cross_up, R.drawable.ic_action_volume_up, R.string.volume_up));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_VOLUME_DOWN, R.drawable.ic_function_volume_cross_down, R.drawable.ic_action_volume_down, R.string.volume_down));
        return functionIconList;
    }

    public static ArrayList<ItemFunctionIcon> getListFloatingIcon() {
        ArrayList<ItemFunctionIcon> functionIconList = new ArrayList<>();
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_NONE, R.drawable.ic_function_none, R.drawable.ic_action_none, R.string.none));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_BACK, R.drawable.ic_function_back, R.drawable.ic_action_back, R.string.back));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_HOME, R.drawable.ic_function_home, R.drawable.ic_action_home, R.string.home));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_OPEN_MENU, R.drawable.ic_function_open_menu, R.drawable.ic_action_airplane, R.string.open_menu));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_SCREEN_RECORDER, R.drawable.ic_function_screen_recorder, R.drawable.ic_action_video_recorder, R.string.screen_recorder));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_FLASHLIGHT, R.drawable.ic_function_flashlight, R.drawable.ic_action_flashlight, R.string.flashlight));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_CAMERA, R.drawable.ic_function_camera, R.drawable.ic_action_all_app, R.string.camera));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_SCREEN_SHOT, R.drawable.ic_function_screen_shoot, R.drawable.ic_action_screen_shoot, R.string.screen_shoot));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_NOTIFICATION, R.drawable.ic_function_notification, R.drawable.ic_action_notification, R.string.notification));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_RECENT, R.drawable.ic_function_recent, R.drawable.ic_action_recent, R.string.recent));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_LOCK_SCREEN, R.drawable.ic_function_lock_screen, R.drawable.ic_action_lock, R.string.lock_screen));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_POWER, R.drawable.ic_function_power, R.drawable.ic_action_power, R.string.power));
        functionIconList.add(new ItemFunctionIcon(ItemFunctionIcon.ACTION_ALL_APP, R.drawable.ic_function_all_app, R.drawable.ic_action_all_app, R.string.all_app));
        return functionIconList;
    }
}
