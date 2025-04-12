package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.timeline;

import com.google.gson.annotations.SerializedName;

public class OddDetail {
    @SerializedName("label")
    public String label;
    @SerializedName("probability")
    public String probability;

    public OddDetail() {
    }

    @Override
    public String toString() {
        return "OddDetail{" +
                "label='" + label + '\'' +
                ", probability='" + probability + '\'' +
                '}';
    }
}
