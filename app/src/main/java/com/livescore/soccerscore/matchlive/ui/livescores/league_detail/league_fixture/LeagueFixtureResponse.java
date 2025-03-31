package com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_fixture;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.fixture.TeamFixtureDetail;

public class LeagueFixtureResponse {
    @SerializedName("data")
    public Object data;

    public LeagueFixtureDetail getData() {
        return new Gson().fromJson(new Gson().toJson(data), LeagueFixtureDetail.class);
    }

    @Override
    public String toString() {
        return "TeamFixtureResponse{" +
                "data=" + getData() +
                '}';
    }
}
