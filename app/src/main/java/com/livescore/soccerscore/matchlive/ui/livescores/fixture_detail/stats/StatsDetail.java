package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.stats;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueDetail;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_table.StandingDetail;

public class StatsDetail {
    @SerializedName("type_id")
    public long type_id;
    @SerializedName("participant_id")
    public long participant_id;
    @SerializedName("location")
    public String location;
    @SerializedName("type")
    public Object type;
    @SerializedName("data")
    public Object data;

    public StandingDetail.Type getType() {
        return new Gson().fromJson(new Gson().toJson(type), StandingDetail.Type.class);
    }

    public Data getData() {
        return new Gson().fromJson(new Gson().toJson(data), Data.class);
    }

    @Override
    public String toString() {
        return "StatsDetail{" +
                "type_id=" + type_id +
                ", participant_id=" + participant_id +
                ", location='" + location + '\'' +
                ", type=" + getType() +
                ", data=" + getData() +
                '}';
    }

    public static class Data {
        @SerializedName("value")
        public long value;

        @Override
        public String toString() {
            return "Data{" +
                    "value=" + value +
                    '}';
        }
    }

}
