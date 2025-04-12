package com.livescore.soccerscore.matchlive.model.league.detail;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class LeagueFixtureResponse {
    @SerializedName("data")
    public Object data;

    public LeagueFixtureDetail getData() {
        return new Gson().fromJson(new Gson().toJson(data), LeagueFixtureDetail.class);
    }

    public LeagueFixtureResponse() {
    }

    @Override
    public String toString() {
        return "TeamFixtureResponse{" +
                "data=" + getData() +
                '}';
    }
}
