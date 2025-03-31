package com.livescore.soccerscore.matchlive.api_data.model;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

import java.util.List;

public class StateResponse {
    @SerializedName("data")
    public List<FixtureModel.StateModel> states;

    @Override
    public String toString() {
        return "StateResponse{" +
                "states=" + states +
                '}';
    }
}
