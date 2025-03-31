package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.fixture;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamInMatch;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;

import java.util.List;

public class TeamFixtureDetail extends TeamModel {
    @SerializedName("upcoming")
    List<FixtureModel> upcoming;
//    @SerializedName("inplay")
//    List<FixtureModel> inplay;

    @Override
    public String toString() {
        return "TeamFixtureDetail{" +
                "upcoming=" + upcoming +
//                ", inplay=" + inplay +
                '}';
    }
}
