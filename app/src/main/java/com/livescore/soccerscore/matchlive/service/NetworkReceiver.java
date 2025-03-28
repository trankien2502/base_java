package com.livescore.soccerscore.matchlive.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.ui.livescores.NoInternetActivity;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
            // Nếu có internet, gửi broadcast để đóng activity
            if (NoInternetActivity.instance != null) {
                NoInternetActivity.instance.finish();
            }
        } else {
            if (NoInternetActivity.instance == null) {
                Intent noInternetIntent = new Intent(context, NoInternetActivity.class);
                context.startActivity(noInternetIntent);
            }
        }
    }

    private boolean isNetworkAvailable(Context context) {
        return IsNetWork.haveNetworkConnection(context);
    }
}
