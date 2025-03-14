package com.assistivetouch.easytouch.homebutton.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StopServiceRecordReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("STOP_SERVICE".equals(intent.getAction())) {
            Intent stopIntent = new Intent(context, ScreenRecordService.class);
            context.stopService(stopIntent);
        }
    }
}
