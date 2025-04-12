package com.livescore.soccerscore.matchlive.model.fixture;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.model.league.LeagueTodayModel;

import java.util.List;

public class FixtureResponse {
    @SerializedName("data")
    public List<LeagueTodayModel> data;
    @SerializedName("pagination")
    public Object pagination;

    public FixtureResponse() {
    }

    @Override
    public String toString() {
        return "FixtureResponse{" +
                "data=" + data +
                ", pagination=" + pagination +
                '}';
    }
}
