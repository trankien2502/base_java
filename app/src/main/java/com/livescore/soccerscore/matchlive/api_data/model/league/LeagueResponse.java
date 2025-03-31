package com.livescore.soccerscore.matchlive.api_data.model.league;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeagueResponse {
    @SerializedName("data")
    public List<LeagueDetail> data;

    @SerializedName("pagination")
    public Object pagination;

    @Override
    public String toString() {
        return "LeagueResponse{" +
                "data=" + data +
                ", pagination=" + pagination +
                '}';
    }
}

