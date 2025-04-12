package com.livescore.soccerscore.matchlive.ui.livescores.home;

import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;

public interface FixtureClickCallBack {
    void select(int pos, FixtureModel fixtureModel);

    void pin(int pos, FixtureModel fixtureModel);

    void alarm(int pos, FixtureModel fixtureModel);
}
