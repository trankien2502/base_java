package com.tkt.basejava.basejava1.basejava2.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;

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
        if (instance != null) {
            instance.performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN); // Khóa màn hình
        }
    }
}
