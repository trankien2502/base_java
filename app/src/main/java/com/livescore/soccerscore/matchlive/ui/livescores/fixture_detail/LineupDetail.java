package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.squad.SquadModel;

public class LineupDetail {
    @SerializedName("team_id")
    public long team_id;
    @SerializedName("formation_field")
    public String formation_field;
    @SerializedName("player_name")
    public String player_name;
    @SerializedName("formation_position")
    public int formation_position;
    @SerializedName("position")
    public Object position;
    @SerializedName("player")
    public Object player;

    public SquadModel.Position getPosition() {
        return new Gson().fromJson(new Gson().toJson(position), SquadModel.Position.class);
    }

    public SquadModel.Player getPlayer() {
        return new Gson().fromJson(new Gson().toJson(player), SquadModel.Player.class);
    }

    @Override
    public String toString() {
        return "LineupDetail{" +
                "team_id=" + team_id +
                ", formation_field='" + formation_field + '\'' +
                ", player_name='" + player_name + '\'' +
                ", formation_position=" + formation_position +
                ", position=" + getPosition() +
                ", player=" + getPlayer() +
                '}';
    }
}
