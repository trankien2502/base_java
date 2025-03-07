package com.assistivetouch.easytouch.homebutton.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;


import java.lang.reflect.Method;


public class CheckUtils {
    public static String getTypeNetwork(int i) {
        if (i != 0) {
            return i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? "5G" : "TD-SCDMA" : "WCDMA" : "LTE" : "CDMA" : "GSM";
        }
        return null;
    }

    public static boolean checkPer(Context context, String str) {
        return ActivityCompat.checkSelfPermission(context, str) == 0;
    }

    public static boolean canWriteInMediaStore(Context context) {
        return Build.VERSION.SDK_INT >= 29 || ActivityCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public static boolean checkCanDrawOtherApp(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }

    public static boolean isNotificationServiceRunning(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        return string != null && string.contains(context.getPackageName());
    }

    public static boolean checkSystemWriteSetting(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return Settings.System.canWrite(context);
        }
        return true;
    }

    public static boolean isAccessibilitySettingsOn(Context context, Class<?> serviceClass) {
        int i;
        String string;
        String str = context.getPackageName() + "/" + serviceClass.getCanonicalName();
        try {
            i = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), "accessibility_enabled");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            i = 0;
        }
        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
        if (i == 1 && (string = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), "enabled_accessibility_services")) != null) {
            simpleStringSplitter.setString(string);
            while (simpleStringSplitter.hasNext()) {
                if (simpleStringSplitter.next().equalsIgnoreCase(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getNameNetwork(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
    }

    public static boolean checkSystemWriteSettingEndAction(Context context) {
        boolean checkSystemWriteSetting = checkSystemWriteSetting(context);
        if (!checkSystemWriteSetting && Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
        return checkSystemWriteSetting;
    }

    public static void setAutoOrientationEnabled(boolean z, Context context) {
        Settings.System.putInt(context.getContentResolver(), "accelerometer_rotation", z ? 1 : 0);
    }

    public static boolean checkAirplane(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), "airplane_mode_on") == 1;
        } catch (Settings.SettingNotFoundException unused) {
            return false;
        }
    }

    public static boolean hasMobileData(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Method declaredMethod = Class.forName(connectivityManager.getClass().getName()).getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            declaredMethod.setAccessible(true);
            return ((Boolean) declaredMethod.invoke(connectivityManager, new Object[0])).booleanValue();
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isApOn(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            try {
                Method declaredMethod = wifiManager.getClass().getDeclaredMethod("getWifiApState", new Class[0]);
                declaredMethod.setAccessible(true);
                Object[] objArr = null;
                int intValue = ((Integer) declaredMethod.invoke(wifiManager, null)).intValue();
                if (intValue == 12 || intValue == 13) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
