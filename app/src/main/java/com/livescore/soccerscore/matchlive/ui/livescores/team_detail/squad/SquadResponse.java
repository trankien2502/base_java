package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.squad;

import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueTodayModel;

import java.util.List;

public class SquadResponse {
    @SerializedName("data")
    public List<SquadModel> data;
    @SerializedName("pagination")
    public Object pagination;
    @SerializedName("timezone")
    public String timezone;

    @Override
    public String toString() {
        return "FixtureResponse{" +
                "data=" + data +
                ", pagination=" + pagination +
                ", timezone=" + timezone +
                '}';
    }
}
