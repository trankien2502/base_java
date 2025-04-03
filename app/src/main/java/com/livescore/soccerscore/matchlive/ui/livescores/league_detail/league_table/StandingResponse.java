package com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_table;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StandingResponse {
    @SerializedName("data")
    public List<StandingModel> data;
    @SerializedName("pagination")
    public Object pagination;
}
