package com.livescore.soccerscore.matchlive.api_data.model.team;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class TeamInMatch extends TeamModel {
    @SerializedName("meta")
    public Object meta;

    public Meta getMeta() {
        return new Gson().fromJson(new Gson().toJson(meta), Meta.class);
    }

    @Override
    public String toString() {
        return "TeamInMatch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image_path='" + image_path + '\'' +
                ", isFavourite=" + isFavourite +
                ", meta=" + getMeta() +
                '}';
    }

    public static class Meta {
        @SerializedName("location")
        public String location;
        @SerializedName("winner")
        public boolean winner;
        @SerializedName("position")
        public int position;

        @Override
        public String toString() {
            return "Meta{" +
                    "location='" + location + '\'' +
                    ", winner=" + winner +
                    ", position=" + position +
                    '}';
        }
    }
}
