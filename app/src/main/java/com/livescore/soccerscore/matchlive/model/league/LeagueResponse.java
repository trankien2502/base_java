package com.livescore.soccerscore.matchlive.model.league;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeagueResponse {
    @SerializedName("data")
    public List<LeagueDetail> data;

    @SerializedName("pagination")
    public Object pagination;

    public LeagueResponse() {
    }

    @Override
    public String toString() {
        return "LeagueResponse{" +
                "data=" + data +
                ", pagination=" + pagination +
                '}';
    }
}

