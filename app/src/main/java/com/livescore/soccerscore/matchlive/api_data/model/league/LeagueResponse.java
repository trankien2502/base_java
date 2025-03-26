package com.livescore.soccerscore.matchlive.api_data.model.league;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.PaginationModel;

import java.util.List;

public class LeagueResponse {
    @SerializedName("data")
    public List<LeagueModel> data;

    @SerializedName("pagination")
    public Object pagination;

}

