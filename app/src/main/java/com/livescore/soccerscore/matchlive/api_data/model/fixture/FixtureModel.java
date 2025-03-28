package com.livescore.soccerscore.matchlive.api_data.model.fixture;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.ScoreModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamInMatch;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;

import java.util.List;

public class FixtureModel extends FixtureBase{
    @SerializedName("participants")
    public List<TeamInMatch> participants;
    @SerializedName("scores")
    public List<ScoreModel> scores;
    @SerializedName("state")
    public Object state;

    public StateModel getState() {
        Gson gson = new Gson();
        return gson.fromJson(new Gson().toJson(state), StateModel.class);
    }


    @Override
    public String toString() {
        return "FixtureModel{" +
                "id=" + id +
                ", league_id=" + league_id +
                ", name='" + name + '\'' +
                ", starting_at='" + starting_at + '\'' +
                ", result_info='" + result_info + '\'' +
                ", length=" + length +
                ", participants=" + participants +
                ", scores=" + scores +
                ", state=" + getState() +
                '}';
    }
    public static class StateModel {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("state")
        public String state;

        @Override
        public String toString() {
            return "StateModel{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", state='" + state + '\'' +
                    '}';
        }
    }
}
