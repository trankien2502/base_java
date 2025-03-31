package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.squad;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class SquadModel {
    @SerializedName("id")
    public int id;
    @SerializedName("captain")
    public boolean captain;
    @SerializedName("jersey_number")
    public int jersey_number;
    @SerializedName("player")
    public Object player;
    @SerializedName("position")
    public Object position;

    public Player getPlayer() {
        return new Gson().fromJson(new Gson().toJson(player), Player.class);
    }

    public Position getPosition() {
        return new Gson().fromJson(new Gson().toJson(position), Position.class);
    }

    public static class Position {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;

        @Override
        public String toString() {
            return "Position{" +
                    "id=" + id +
                    ", name=" + name +
                    '}';
        }
    }

    public static class Player {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("common_name")
        public String common_name;
        @SerializedName("display_name")
        public String display_name;
        @SerializedName("image_path")
        public String image_path;

        @Override
        public String toString() {
            return "Player{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", common_name='" + common_name + '\'' +
                    ", display_name='" + display_name + '\'' +
                    ", image_path='" + image_path + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SquadModel{" +
                "id=" + id +
                ", captain=" + captain +
                ", jersey_number=" + jersey_number +
                ", player=" + player +
                ", position=" + position +
                '}';
    }
}
