package com.livescore.soccerscore.matchlive.model.fixture;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class TeamFixtureResponse {
    @SerializedName("data")
    public Object data;

    public TeamFixtureDetail getData() {
        return new Gson().fromJson(new Gson().toJson(data), TeamFixtureDetail.class);
    }

    @Override
    public String toString() {
        return "TeamFixtureResponse{" +
                "data=" + data +
                '}';
    }

    public TeamFixtureResponse() {
    }
}
