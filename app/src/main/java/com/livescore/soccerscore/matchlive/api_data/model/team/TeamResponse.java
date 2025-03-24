package com.livescore.soccerscore.matchlive.api_data.model.team;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;

import java.util.List;

public class TeamResponse {
    @SerializedName("data")
    private List<TeamModel> data;

    public List<TeamModel> getData() {
        return data;
    }
}
