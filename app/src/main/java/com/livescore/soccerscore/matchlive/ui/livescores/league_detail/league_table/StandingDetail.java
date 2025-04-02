package com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_table;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class StandingDetail {
    @SerializedName("type_id")
    public int type_id;
    @SerializedName("value")
    public int value;
    @SerializedName("type")
    public Object type;

    public Type getType() {
        return new Gson().fromJson(new Gson().toJson(type), Type.class);
    }

    @Override
    public String toString() {
        return "StandingDetail{" +
                ", type_id=" + type_id +
                ", value=" + value +
                ", type=" + getType() +
                '}';
    }

    public static class Type {
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;
        @SerializedName("code")
        public String code;
        @SerializedName("developer_name")
        public String developer_name;
        @SerializedName("model_type")
        public String model_type;
        @SerializedName("stat_group")
        public String stat_group;

        @Override
        public String toString() {
            return "Type{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", code='" + code + '\'' +
                    ", developer_name='" + developer_name + '\'' +
                    ", model_type='" + model_type + '\'' +
                    ", stat_group='" + stat_group + '\'' +
                    '}';
        }
    }
}
