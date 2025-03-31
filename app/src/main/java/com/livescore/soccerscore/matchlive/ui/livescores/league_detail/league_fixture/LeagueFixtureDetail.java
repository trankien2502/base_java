package com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_fixture;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

import java.util.List;

public class LeagueFixtureDetail {
    @SerializedName("upcoming")
    List<FixtureModel> upcoming;
    @SerializedName("inplay")
    List<FixtureModel> inplay;

    @Override
    public String toString() {
        return "TeamFixtureDetail{" +
                "upcoming=" + upcoming +
                ", inplay=" + inplay +
                '}';
    }
}
