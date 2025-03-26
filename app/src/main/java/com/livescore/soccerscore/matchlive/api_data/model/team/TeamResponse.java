package com.livescore.soccerscore.matchlive.api_data.model.team;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.PaginationModel;

import java.util.List;

public class TeamResponse {
    @SerializedName("data")
    public List<TeamModel> data;
    @SerializedName("subscription")
    public List<Object> subscription;
    @SerializedName("pagination")
    public Object pagination;
    @SerializedName("rate_limit")
    public Object rate_limit;
    @SerializedName("timezone")
    public String timezone;

}
