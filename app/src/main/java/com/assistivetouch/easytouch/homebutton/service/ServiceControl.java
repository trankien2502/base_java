package com.assistivetouch.easytouch.homebutton.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import androidx.core.graphics.PathParser;

import java.lang.reflect.Method;

public class ServiceControl extends AccessibilityService {
    public static ServiceControl instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this; // Lưu lại instance của service
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    public void turnOffScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dispatchGesture(
                    new GestureDescription.Builder()
                            .addStroke(new GestureDescription.StrokeDescription(
                                    PathParser.createPathFromPathData("M500,500 L500,500"), 0, 1))
                            .build(),
                    null, null
            );
        } else {
            Log.e("check_admin","api<24");
        }
    }
}
