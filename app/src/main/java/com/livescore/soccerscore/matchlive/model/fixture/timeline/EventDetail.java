package com.livescore.soccerscore.matchlive.model.fixture.timeline;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.model.league.detail.StandingDetail;
import com.livescore.soccerscore.matchlive.model.live.PeriodModel;

public class EventDetail {
    @SerializedName("id")
    public String id;
    @SerializedName("participant_id")
    public long participant_id;
    @SerializedName("player_name")
    public String player_name;
    @SerializedName("related_player_name")
    public String related_player_name;
    @SerializedName("result")
    public String result;
    @SerializedName("info")
    public String info;
    @SerializedName("addition")
    public String addition;
    @SerializedName("minute")
    public int minute;
    @SerializedName("extra_minute")
    public int extra_minute;
    @SerializedName("sort_order")
    public int sort_order;
    @SerializedName("type")
    public Object type;
    @SerializedName("period")
    public Object period;

    public StandingDetail.Type getType() {
        return new Gson().fromJson(new Gson().toJson(type), StandingDetail.Type.class);
    }

    public PeriodModel getPeriod() {
        return new Gson().fromJson(new Gson().toJson(period), PeriodModel.class);
    }

    public EventDetail() {
    }

    @Override
    public String toString() {
        return "EventDetail{" +
                "participant_id=" + participant_id +
                ", id='" + id + '\'' +
                ", player_name='" + player_name + '\'' +
                ", related_player_name='" + related_player_name + '\'' +
                ", result='" + result + '\'' +
                ", info='" + info + '\'' +
                ", addition='" + addition + '\'' +
                ", minute=" + minute +
                ", extra_minute=" + extra_minute +
                ", sort_order=" + sort_order +
                ", type=" + getType() +
                ", period=" + getPeriod() +
                '}';
    }
}
