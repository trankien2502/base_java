package com.assistivetouch.easytouch.homebutton.service;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
    }
}
