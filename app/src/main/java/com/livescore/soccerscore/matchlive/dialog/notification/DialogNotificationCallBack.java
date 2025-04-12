package com.livescore.soccerscore.matchlive.dialog.notification;

import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;

public interface DialogNotificationCallBack {
    void cancel();
    void save(FixtureModel fixtureModel);
}
