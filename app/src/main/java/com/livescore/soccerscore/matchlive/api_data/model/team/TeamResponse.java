package com.livescore.soccerscore.matchlive.api_data.model.team;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.PaginationModel;

import java.util.List;

public class TeamResponse {
    @SerializedName("data")
    public List<TeamInMatch> data;
    @SerializedName("pagination")
    public Object pagination;

    @Override
    public String toString() {
        return "TeamResponse{" +
                "data=" + data +
                ", pagination=" + pagination +
                '}';
    }
}
