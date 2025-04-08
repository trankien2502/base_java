package com.livescore.soccerscore.matchlive.api_data.model.fixture;

import androidx.room.Entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.ScoreModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamInMatch;

import java.util.List;

@Entity(tableName = "fixture")
public class FixtureModel extends FixtureBase {
    @SerializedName("participants")
    public List<TeamInMatch> participants;
    @SerializedName("scores")
    public List<ScoreModel> scores;
    @SerializedName("state")
    public Object state;
    @SerializedName("start_match")
    public boolean start_match;
    @SerializedName("end_first_half")
    public boolean end_first_half;
    @SerializedName("start_second_half")
    public boolean start_second_half;
    @SerializedName("goals")
    public boolean goals;
    @SerializedName("red_card")
    public boolean red_card;
    @SerializedName("end_match")
    public boolean end_match;
    @SerializedName("before_match")
    public int before_match = -1;


    public StateModel getState() {
        Gson gson = new Gson();
        if (state != null) {
            return gson.fromJson(new Gson().toJson(state), StateModel.class);
        } else return new StateModel();

    }

    public void setState(StateModel stateModel) {
        this.state = stateModel;
    }


    @Override
    public String toString() {
        return "FixtureModel{" +
                "id=" + id +
                "season_id=" + season_id +
                "state_id=" + state_id +
                ", league_id=" + league_id +
                ", name='" + name + '\'' +
                ", starting_at='" + starting_at + '\'' +
                ", result_info='" + result_info + '\'' +
                ", has_odds=" + has_odds +
                ", length=" + length +
                ", isAlarm=" + isAlarm +
                ", isPin=" + isPin +
                ", participants=" + participants +
                ", scores=" + scores +

                ", state=" + getState() +
                '}';
    }

    public static class StateModel {
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;
        @SerializedName("state")
        public String state;
        @SerializedName("short_name")
        public String short_name;

        @Override
        public String toString() {
            return "StateModel{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", short_name='" + short_name + '\'' +
                    ", state='" + state + '\'' +
                    '}';
        }
    }


}
