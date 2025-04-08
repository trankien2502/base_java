package com.livescore.soccerscore.matchlive.dialog.notification;

import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

public interface DialogNotificationCallBack {
    void cancel();
    void save(FixtureModel fixtureModel);
}
