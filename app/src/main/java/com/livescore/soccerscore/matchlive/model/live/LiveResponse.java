package com.livescore.soccerscore.matchlive.model.live;

import com.google.gson.annotations.SerializedName;

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

    public LiveResponse() {
    }
}
