package com.livescore.soccerscore.matchlive.model.fixture.timeline;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class FixtureDetailResponse {
    @SerializedName("data")
    public Object data;

    public FixtureDetailModel getData() {
        return new Gson().fromJson(new Gson().toJson(data), FixtureDetailModel.class);
    }

    public FixtureDetailResponse() {
    }

    @Override
    public String toString() {
        return "FixtureDetailResponse{" +
                "data=" + getData() +
                '}';
    }
}
