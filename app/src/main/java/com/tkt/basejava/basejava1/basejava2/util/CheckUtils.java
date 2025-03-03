package com.tkt.basejava.basejava1.basejava2.util;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
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

    public static boolean isAccessibilitySettingsOn(Context context, Class<AccessibilityService> serviceClass) {
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

//    public static ItemStrengthMobile[] getStrengthMobile(Context context) {
//        int i = 0;
//        ItemStrengthMobile[] itemStrengthMobileArr = {new ItemStrengthMobile(), new ItemStrengthMobile()};
//        if (ActivityCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0) {
//            try {
//                for (CellInfo cellInfo : ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getAllCellInfo()) {
//                    if (cellInfo.isRegistered()) {
//                        if (cellInfo instanceof CellInfoGsm) {
//                            itemStrengthMobileArr[i].setLevel(((CellInfoGsm) cellInfo).getCellSignalStrength().getLevel());
//                            itemStrengthMobileArr[i].setMobile(1);
//                        } else if (cellInfo instanceof CellInfoCdma) {
//                            itemStrengthMobileArr[i].setLevel(((CellInfoCdma) cellInfo).getCellSignalStrength().getLevel());
//                            itemStrengthMobileArr[i].setMobile(2);
//                        } else if (cellInfo instanceof CellInfoLte) {
//                            itemStrengthMobileArr[i].setLevel(((CellInfoLte) cellInfo).getCellSignalStrength().getLevel());
//                            itemStrengthMobileArr[i].setMobile(3);
//                        } else if (cellInfo instanceof CellInfoWcdma) {
//                            itemStrengthMobileArr[i].setLevel(((CellInfoWcdma) cellInfo).getCellSignalStrength().getLevel());
//                            itemStrengthMobileArr[i].setMobile(4);
//                        } else if (Build.VERSION.SDK_INT >= 29) {
//                            if (cellInfo instanceof CellInfoTdscdma) {
//                                itemStrengthMobileArr[i].setLevel(((CellInfoTdscdma) cellInfo).getCellSignalStrength().getLevel());
//                                itemStrengthMobileArr[i].setMobile(5);
//                            } else if (cellInfo instanceof CellInfoNr) {
//                                itemStrengthMobileArr[i].setLevel(((CellInfoNr) cellInfo).getCellSignalStrength().getLevel());
//                                itemStrengthMobileArr[i].setMobile(6);
//                            }
//                        }
//                        i++;
//                        if (i >= 2) {
//                            break;
//                        }
//                    }
//                }
//            } catch (Exception unused) {
//            }
//        }
//        return itemStrengthMobileArr;
//    }

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
