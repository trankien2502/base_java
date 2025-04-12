package com.livescore.soccerscore.matchlive.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class ScoreModel {
    @SerializedName("description")
    public String description;
    @SerializedName("id")
    public long id;
    @SerializedName("fixture_id")
    public int fixture_id;
    @SerializedName("type_id")
    public int type_id;
    @SerializedName("participant_id")
    public int participant_id;
    @SerializedName("score")
    public Object score;

    public ScoreModel() {
    }

    @Override
    public String toString() {
        return "ScoreModel{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", fixture_id=" + fixture_id +
                ", type_id=" + type_id +
                ", participant_id=" + participant_id +
                ", score=" + getScore() +
                '}';
    }

    public Score getScore() {
        Gson gson = new Gson();
        return gson.fromJson(new Gson().toJson(score), Score.class);
    }


    public static class Score {
        @SerializedName("goals")
        public int goals;
        @SerializedName("participant")
        public String participant;

        @Override
        public String toString() {
            return "Score{" +
                    "goals=" + goals +
                    ", participant='" + participant + '\'' +
                    '}';
        }
    }
}
