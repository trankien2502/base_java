package com.livescore.soccerscore.matchlive.api_data.model.team;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

public class TeamInMatch extends TeamModel {
    @SerializedName("meta")
    public Object meta;
    @SerializedName("country")
    public Object country;
    public Meta getMeta() {
        return new Gson().fromJson(new Gson().toJson(meta), Meta.class);
    }
    public Country getCountry() {
        Gson gson = new Gson();
        if (country != null) {
            return gson.fromJson(new Gson().toJson(country), Country.class);
        } else return new Country();

    }

    public void setCountry(Country country) {
        this.country = country;
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
    public static class Country {
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;

        @Override
        public String toString() {
            return "Country{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
