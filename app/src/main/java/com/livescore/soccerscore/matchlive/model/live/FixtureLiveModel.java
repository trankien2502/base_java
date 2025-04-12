package com.livescore.soccerscore.matchlive.model.live;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;

import java.util.List;

public class FixtureLiveModel extends FixtureModel {
    @SerializedName("periods")
    public List<PeriodModel> periods;

    @Override
    public String toString() {
        return "FixtureLiveModel{" +
                "has_odds=" + has_odds +
                "season_id=" + season_id +
                "periods=" + periods +
                ", participants=" + participants +
                ", scores=" + scores +
                ", state=" + getState() +
                '}';
    }

    public FixtureLiveModel() {
    }
}
