package com.livescore.soccerscore.matchlive.model;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;

import java.util.List;

public class StateResponse {
    @SerializedName("data")
    public List<FixtureModel.StateModel> states;

    public StateResponse() {
    }

    @Override
    public String toString() {
        return "StateResponse{" +
                "states=" + states +
                '}';
    }
}
