package com.livescore.soccerscore.matchlive.model.team;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamResponse {
    @SerializedName("data")
    public List<TeamInMatch> data;
    @SerializedName("pagination")
    public Object pagination;

    public TeamResponse() {
    }

    @Override
    public String toString() {
        return "TeamResponse{" +
                "data=" + data +
                ", pagination=" + pagination +
                '}';
    }
}
