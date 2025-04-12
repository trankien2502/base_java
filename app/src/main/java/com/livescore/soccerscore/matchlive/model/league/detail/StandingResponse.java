package com.livescore.soccerscore.matchlive.model.league.detail;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StandingResponse {
    @SerializedName("data")
    public List<StandingModel> data;
    @SerializedName("pagination")
    public Object pagination;

    public StandingResponse() {
    }
}
