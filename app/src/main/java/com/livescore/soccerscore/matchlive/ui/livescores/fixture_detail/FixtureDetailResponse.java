package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class FixtureDetailResponse {
    @SerializedName("data")
    Object data;

    public FixtureDetailModel getData() {
        return new Gson().fromJson(new Gson().toJson(data), FixtureDetailModel.class);
    }

    @Override
    public String toString() {
        return "FixtureDetailResponse{" +
                "data=" + getData() +
                '}';
    }
}
