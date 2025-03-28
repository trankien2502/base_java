package com.livescore.soccerscore.matchlive.api_data.model.fixture;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueTodayModel;

import java.util.List;
import java.util.Objects;

public class FixtureResponse {
    @SerializedName("data")
    public List<LeagueTodayModel> data;
    @SerializedName("pagination")
    public Object pagination;

    @Override
    public String toString() {
        return "FixtureResponse{" +
                "data=" + data +
                ", pagination=" + pagination +
                '}';
    }
}
