package com.livescore.soccerscore.matchlive.api_data.model.fixture;


import com.google.gson.annotations.SerializedName;

public class FixtureBase {
    @SerializedName("id")
    public long id;
    @SerializedName("league_id")
    public int league_id;
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
    public boolean isAlarm;
    public boolean isPin;

    @Override
    public String toString() {
        return "FixtureBase{" +
                "id=" + id +
                "state_id=" + state_id +
                ", league_id=" + league_id +
                ", name='" + name + '\'' +
                ", starting_at='" + starting_at + '\'' +
                ", result_info='" + result_info + '\'' +
                ", length=" + length +
                ", isAlarm=" + isAlarm +
                ", isPin=" + isPin +
                '}';
    }
}
