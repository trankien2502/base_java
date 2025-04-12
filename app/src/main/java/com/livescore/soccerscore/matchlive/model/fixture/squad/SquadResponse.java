package com.livescore.soccerscore.matchlive.model.fixture.squad;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SquadResponse {
    @SerializedName("data")
    public List<SquadModel> data;
    @SerializedName("pagination")
    public Object pagination;
    @SerializedName("timezone")
    public String timezone;

    @Override
    public String toString() {
        return "FixtureResponse{" +
                "data=" + data +
                ", pagination=" + pagination +
                ", timezone=" + timezone +
                '}';
    }

    public SquadResponse() {
    }
}
