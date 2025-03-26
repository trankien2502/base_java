package com.livescore.soccerscore.matchlive.api_data.model.fixture;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class FixtureResponse {
    @SerializedName("data")
    public List<Object> data;
    @SerializedName("pagination")
    public Object pagination;
}
