package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueDetail;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamInMatch;
import com.livescore.soccerscore.matchlive.ui.livescores.live.FixtureLiveModel;

import java.util.List;

public class FixtureDetailModel extends FixtureLiveModel {
    @SerializedName("league")
    public Object league;
    @SerializedName("venue")
    public Object venue;
    @SerializedName("events")
    public List<EventDetail> events;
    @SerializedName("lineups")
    public List<LineupDetail> lineups;

    public LeagueDetail getLeague() {
        return new Gson().fromJson(new Gson().toJson(league), LeagueDetail.class);
    }

    public Venue getVenue() {
        return new Gson().fromJson(new Gson().toJson(venue), Venue.class);
    }

    public static class Venue {
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;
        @SerializedName("address")
        public String address;
        @SerializedName("image_path")
        public String image_path;
        @SerializedName("city_name")
        public String city_name;

        @Override
        public String toString() {
            return "Venue{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", image_path='" + image_path + '\'' +
                    ", city_name='" + city_name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FixtureDetailModel{" +
                "league=" + getLeague() +
                ", venue=" + getVenue() +
                ", events=" + events +
                ", lineups=" + lineups +
                ", participants=" + participants +
                ", scores=" + scores +
                ", state=" + state +
                ", id=" + id +
                ", league_id=" + league_id +
                ", state_id=" + state_id +
                ", name='" + name + '\'' +
                ", starting_at='" + starting_at + '\'' +
                ", result_info='" + result_info + '\'' +
                '}';
    }
}
