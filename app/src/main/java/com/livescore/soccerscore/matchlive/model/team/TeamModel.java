package com.livescore.soccerscore.matchlive.model.team;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
@Keep
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
