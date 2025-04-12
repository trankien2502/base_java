package com.livescore.soccerscore.matchlive.model.fixture.stats;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SeasonResponse {
    @SerializedName("data")
    public List<SeasonDetail> data;
    @SerializedName("pagination")
    public Object pagination;

    public SeasonResponse() {
    }

    @Override
    public String toString() {
        return "SeasonResponse{" +
                "data=" + data +
                ", pagination=" + pagination +
                '}';
    }
}
