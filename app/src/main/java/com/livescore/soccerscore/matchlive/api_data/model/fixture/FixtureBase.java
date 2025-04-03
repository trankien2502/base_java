package com.livescore.soccerscore.matchlive.api_data.model.fixture;


import com.google.gson.annotations.SerializedName;

public class FixtureBase {
    @SerializedName("id")
    public long id;
    @SerializedName("league_id")
    public int league_id;
    @SerializedName("season_id")
    public long season_id;
    @SerializedName("state_id")
    public int state_id;
    @SerializedName("name")
    public String name;
    @SerializedName("starting_at")
    public String starting_at;
    @SerializedName("result_info")
    public String result_info;
    @SerializedName("length")
    public int length;
    @SerializedName("has_odds")
    public boolean has_odds;
    public boolean isAlarm;
    public boolean isPin;

    @Override
    public String toString() {
        return "FixtureBase{" +
                "id=" + id +
                ", league_id=" + league_id +
                ", season_id=" + season_id +
                ", state_id=" + state_id +
                ", name='" + name + '\'' +
                ", starting_at='" + starting_at + '\'' +
                ", result_info='" + result_info + '\'' +
                ", length=" + length +
                ", has_odds=" + has_odds +
                ", isAlarm=" + isAlarm +
                ", isPin=" + isPin +
                '}';
    }
}
