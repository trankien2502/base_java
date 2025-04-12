package com.livescore.soccerscore.matchlive.model.team;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "team")
public class TeamModel implements Serializable {
    @PrimaryKey
    @SerializedName("id")
    long id;
    @SerializedName("name")
    String name;
    @SerializedName("image_path")
    String image_path;
    public String countryName;
    @SerializedName("isFavourite")
    boolean isFavourite;

    public TeamModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

//    public Meta getMeta() {
//        return meta;
//    }
//
//    public void setMeta(Meta meta) {
//        this.meta = meta;
//    }


    @Override
    public String toString() {
        return "TeamModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image_path='" + image_path + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }


}
