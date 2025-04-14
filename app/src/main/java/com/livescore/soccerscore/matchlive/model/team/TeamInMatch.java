package com.livescore.soccerscore.matchlive.model.team;

import androidx.annotation.Keep;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class TeamInMatch extends TeamModel {
    @SerializedName("meta")
    public Meta meta;
    @SerializedName("country")
    public Country country;

    public Meta getMeta() {
//        return new Gson().fromJson(new Gson().toJson(meta), Meta.class);
        return meta;
    }

    public Country getCountry() {
//        Gson gson = new Gson();
//        if (country != null) {
//            return gson.fromJson(new Gson().toJson(country), Country.class);
//        } else return new Country();
        return country;
    }

    public TeamInMatch() {

    }


    @Override
    public String toString() {
        return "TeamInMatch{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + getCountry() + '\'' +
                ", image_path='" + image_path + '\'' +
                ", isFavourite=" + isFavourite +
                ", meta=" + getMeta() +
                '}';
    }

    public static class Meta implements Serializable {
        @SerializedName("location")
        public String location;
        @SerializedName("winner")
        public boolean winner;
        @SerializedName("position")
        public int position;

        public Meta() {
        }

        @Override
        public String toString() {
            return "Meta{" +
                    "location='" + location + '\'' +
                    ", winner=" + winner +
                    ", position=" + position +
                    '}';
        }
    }

    public static class Country implements Serializable{
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;

        public Country() {
        }

        @Override
        public String toString() {
            return "Country{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
