package com.livescore.soccerscore.matchlive.model.score;

import com.google.gson.annotations.SerializedName;

public class Score {
    @SerializedName("goals")
    public int goals;
    @SerializedName("participant")
    public String participant;

    public Score() {
    }

    @Override
    public String toString() {
        return "Score{" +
                "goals=" + goals +
                ", participant='" + participant + '\'' +
                '}';
    }
}
