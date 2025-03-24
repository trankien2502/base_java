package com.livescore.soccerscore.matchlive.api_data.model.team;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class TeamModel {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("image_path")
    String image_path;
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
