package com.livescore.soccerscore.matchlive.api_data.model.league;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;

import java.util.List;

public class LeagueTodayModel extends LeagueModel{
    @SerializedName("today")
    public List<FixtureModel> today;

    public List<FixtureModel> getToday() {
        return today;
    }

    public void setToday(List<FixtureModel> today) {
        this.today = today;
    }

    @Override
    public String toString() {
        return "LeagueTodayModel{" +
                "id=" + id +
                ", sport_id=" + sport_id +
                ", country_id=" + country_id +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", image_path='" + image_path + '\'' +
                ", isFavourite=" + isFavourite +
                ", today=" + today +
                '}';
    }
}
