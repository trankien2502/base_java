package com.livescore.soccerscore.matchlive.model.league;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;

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

    public LeagueTodayModel() {
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
