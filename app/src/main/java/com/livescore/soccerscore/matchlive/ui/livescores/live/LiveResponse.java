package com.livescore.soccerscore.matchlive.ui.livescores.live;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

import java.util.List;

public class LiveResponse {
    @SerializedName("data")
    public List<FixtureLiveModel> data;
    @SerializedName("timezone")
    public String timezone;

    @Override
    public String toString() {
        return "LiveResponse{" +
                "data=" + data +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
