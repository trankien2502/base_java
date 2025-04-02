package com.livescore.soccerscore.matchlive.api_data.model.league;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "league")
public class LeagueModel implements Serializable {
    @PrimaryKey
    @SerializedName("id")
    public long id;
    @SerializedName("sport_id")
    public int sport_id;
    @SerializedName("country_id")
    public int country_id;
    @SerializedName("name")
    public String name;
    @SerializedName("active")
    public boolean active;
    @SerializedName("image_path")
    public String image_path;
    @SerializedName("isFavourite")
    public boolean isFavourite;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getSport_id() {
        return sport_id;
    }

    public void setSport_id(int sport_id) {
        this.sport_id = sport_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public String toString() {
        return "LeagueModel{" +
                "id=" + id +
                ", sport_id=" + sport_id +
                ", country_id=" + country_id +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", image_path='" + image_path + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }
}
