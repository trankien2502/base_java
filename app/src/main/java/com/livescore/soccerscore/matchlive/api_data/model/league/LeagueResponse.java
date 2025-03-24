package com.livescore.soccerscore.matchlive.api_data.model.league;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeagueResponse {
    @SerializedName("data")
    private List<LeagueModel> data;

    public List<LeagueModel> getData() {
        return data;
    }
}
