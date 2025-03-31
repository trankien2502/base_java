package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.fixture;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

import java.util.List;

public class RoundModel {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("finished")
    public boolean finished;
    @SerializedName("is_current")
    public boolean is_current;
    @SerializedName("starting_at")
    public String starting_at;
    @SerializedName("ending_at")
    public String ending_at;
    @SerializedName("fixtures")
    public List<FixtureModel> fixtures;

    @Override
    public String toString() {
        return "RoundModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", finished=" + finished +
                ", is_current=" + is_current +
                ", starting_at='" + starting_at + '\'' +
                ", ending_at='" + ending_at + '\'' +
                ", fixtures=" + fixtures +
                '}';
    }
}
