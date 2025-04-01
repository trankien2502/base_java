package com.livescore.soccerscore.matchlive.ui.livescores.live;

import com.google.gson.annotations.SerializedName;

public class PeriodModel {
    @SerializedName("id")
    public int id;
    @SerializedName("sort_order")
    public int sort_order;
    @SerializedName("time_added")
    public int time_added;
    @SerializedName("description")
    public String description;
    @SerializedName("minutes")
    public int minutes;
    @SerializedName("seconds")
    public int seconds;
    @SerializedName("has_timer")
    public boolean has_timer;
}
