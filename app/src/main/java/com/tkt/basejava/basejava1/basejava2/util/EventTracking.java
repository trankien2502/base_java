package com.tkt.basejava.basejava1.basejava2.util;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tkt.basejava.basejava1.basejava2.ads.IsNetWork;

public class EventTracking {

    public static void logEvent(Context context, String nameEvent) {
        if (context != null) {
            if (IsNetWork.haveNetworkConnection(context)) {
                Bundle bundle = new Bundle();
                bundle.putString(nameEvent, nameEvent);
                FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle);
            }

        }
    }

    public static void logEvent(Context context, String nameEvent, String param, String value) {
        if (context != null) {
            if (IsNetWork.haveNetworkConnection(context)) {
                Bundle bundle = new Bundle();
                bundle.putString(param, value);
                FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle);
            }
        }
    }

    public static void logEvent(Context context, String nameEvent, String param, Long value) {
        if (context != null) {
            if (IsNetWork.haveNetworkConnection(context)) {
                Bundle bundle = new Bundle();
                bundle.putLong(param, value);
                FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle);
            }
        }
    }

    public static void logEvent(Context context, String nameEvent, Bundle bundle) {
        if (context != null) {
            if (IsNetWork.haveNetworkConnection(context)) {
                FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle);
            }

        }
    }

}
