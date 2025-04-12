package com.livescore.soccerscore.matchlive.model.league.detail;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.model.team.TeamModel;

import java.util.List;

public class StandingModel {
    @SerializedName("season_id")
    public int season_id;
    @SerializedName("position")
    public int position;
    @SerializedName("points")
    public int points;
    @SerializedName("result")
    public String result;
    @SerializedName("participant")
    public Object participant;
    @SerializedName("details")
    public List<StandingDetail> details;

    public TeamModel getParticipant() {
        return new Gson().fromJson(new Gson().toJson(participant), TeamModel.class);
    }

    public StandingModel() {
    }

    @Override
    public String toString() {
        return "StandingModel{" +
                ", season_id=" + season_id +
                ", position=" + position +
                ", points=" + points +
                ", result=" + result +
                ", participant=" + getParticipant() +
                ", details=" + details +
                '}';
    }
}
