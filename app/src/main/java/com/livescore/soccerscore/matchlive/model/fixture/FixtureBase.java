package com.livescore.soccerscore.matchlive.model.fixture;


import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FixtureBase implements Serializable {
    @PrimaryKey
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
    @SerializedName("isAlarm")
    public boolean isAlarm = false;
    @SerializedName("isPin")
    public boolean isPin = false;

    public FixtureBase() {
    }

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
