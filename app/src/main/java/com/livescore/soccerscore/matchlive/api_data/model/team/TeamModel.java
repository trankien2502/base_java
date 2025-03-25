package com.livescore.soccerscore.matchlive.api_data.model.team;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "team")
public class TeamModel {
    @PrimaryKey
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("image_path")
    String image_path;
    @SerializedName("isFavourite")
    boolean isFavourite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    @NonNull
    @Override
    public String toString() {
        return "id = " + id + " name = " + name + " image_path = " + image_path + "\n";

    }
}
