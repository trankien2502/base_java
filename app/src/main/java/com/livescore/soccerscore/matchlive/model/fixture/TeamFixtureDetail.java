package com.livescore.soccerscore.matchlive.model.fixture;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.model.team.TeamModel;

import java.util.List;

public class TeamFixtureDetail extends TeamModel {
    @SerializedName("upcoming")
    public List<FixtureModel> upcoming;
//    @SerializedName("inplay")
//    List<FixtureModel> inplay;

    @Override
    public String toString() {
        return "TeamFixtureDetail{" +
                "upcoming=" + upcoming +
//                ", inplay=" + inplay +
                '}';
    }

    public TeamFixtureDetail() {
    }
}
