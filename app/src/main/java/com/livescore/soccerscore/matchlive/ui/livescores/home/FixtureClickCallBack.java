package com.livescore.soccerscore.matchlive.ui.livescores.home;

import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

public interface FixtureClickCallBack {
    void select(FixtureModel fixtureModel);
    void pin(FixtureModel fixtureModel);
    void alarm(FixtureModel fixtureModel);
}
