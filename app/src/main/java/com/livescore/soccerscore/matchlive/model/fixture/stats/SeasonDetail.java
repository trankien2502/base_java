package com.livescore.soccerscore.matchlive.model.fixture.stats;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.model.league.LeagueDetail;

public class SeasonDetail {
    @SerializedName("id")
    public long id;
    @SerializedName("league_id")
    public long league_id;
    @SerializedName("name")
    public String name;
    @SerializedName("starting_at")
    public String starting_at;
    @SerializedName("ending_at")
    public String ending_at;
    @SerializedName("finished")
    public boolean finished;
    @SerializedName("is_current")
    public boolean is_current;
    @SerializedName("pending")
    public boolean pending;
    @SerializedName("league")
    public Object league;
    public boolean isSelect = false;

    public LeagueDetail getLeague() {
        return new Gson().fromJson(new Gson().toJson(league), LeagueDetail.class);
    }

    public SeasonDetail() {
    }

    @Override
    public String toString() {
        return "SeasonDetail{" +
                "id=" + id +
                ", league_id=" + league_id +
                ", name='" + name + '\'' +
                ", isSelect=" + isSelect +
                ", starting_at='" + starting_at + '\'' +
                ", ending_at='" + ending_at + '\'' +
                ", finished=" + finished +
                ", is_current=" + is_current +
                ", pending=" + pending +
                ", league=" + getLeague() +
                '}';
    }
}
