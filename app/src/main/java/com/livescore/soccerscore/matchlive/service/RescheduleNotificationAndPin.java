package com.livescore.soccerscore.matchlive.service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;

import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureBase;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.database.fixture.FixtureDatabase;

import java.util.List;

public class RescheduleNotificationAndPin extends LifecycleService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        List<FixtureModel> fixtures = FixtureDatabase.getInstance(this).fixtureDAO().getAllFixture();

        for (FixtureBase a : fixtures) {
            if (a.isPin) {
                a.schedulePin(getApplicationContext());
            }
            if (a.isAlarm) {
                a.scheduleSendNotification(getApplicationContext());
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        super.onBind(intent);
        return null;
    }
}
