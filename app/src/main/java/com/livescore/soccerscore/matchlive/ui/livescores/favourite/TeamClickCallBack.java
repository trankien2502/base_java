package com.livescore.soccerscore.matchlive.ui.livescores.favourite;

import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;

public interface TeamClickCallBack {
    void select(TeamModel teamModel);
    void follow(int position,TeamModel teamModel);
    void load();
}
