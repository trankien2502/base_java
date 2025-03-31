package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.fixture;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

import java.util.List;

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
}
