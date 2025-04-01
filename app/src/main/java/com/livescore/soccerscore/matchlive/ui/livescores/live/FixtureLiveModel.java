package com.livescore.soccerscore.matchlive.ui.livescores.live;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

import java.util.List;

public class FixtureLiveModel extends FixtureModel {
    @SerializedName("periods")
    List<PeriodModel> periods;

    @Override
    public String toString() {
        return "FixtureLiveModel{" +
                "periods=" + periods +
                ", participants=" + participants +
                ", scores=" + scores +
                ", state=" + getState() +
                '}';
    }
}
