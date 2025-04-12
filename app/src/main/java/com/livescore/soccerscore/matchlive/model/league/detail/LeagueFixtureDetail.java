package com.livescore.soccerscore.matchlive.model.league.detail;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;

import java.util.List;

public class LeagueFixtureDetail {
    @SerializedName("upcoming")
    public List<FixtureModel> upcoming;
    @SerializedName("inplay")
    public List<FixtureModel> inplay;

    public LeagueFixtureDetail() {
    }

    @Override
    public String toString() {
        return "TeamFixtureDetail{" +
                "upcoming=" + upcoming +
                ", inplay=" + inplay +
                '}';
    }
}
