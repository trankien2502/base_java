package com.livescore.soccerscore.matchlive.model.league;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.model.team.TeamInMatch;

import java.io.Serializable;

public class LeagueDetail extends LeagueModel implements Serializable {
    @SerializedName("country")
    public Object country;

    public TeamInMatch.Country getCountry() {
        Gson gson = new Gson();
        if (country != null) {
            return gson.fromJson(new Gson().toJson(country), TeamInMatch.Country.class);
        } else return new TeamInMatch.Country();

    }

    public void setCountry(TeamInMatch.Country country) {
        this.country = country;
    }

    public LeagueDetail() {
    }

    @Override
    public String toString() {
        return "LeagueDetail{" +
                "country=" + getCountry() +
                ", id=" + id +
                ", sport_id=" + sport_id +
                ", country_id=" + country_id +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", image_path='" + image_path + '\'' +
                ", isFavourite=" + isFavourite +
                '}';
    }
}
